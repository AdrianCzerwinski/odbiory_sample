package com.example.app.data.repository

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.util.Log
import com.example.app.domain.model.*
import com.example.app.domain.repository.Repository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


class RepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : Repository {
    private var user = auth.currentUser

    private val storageRef = FirebaseStorage.getInstance().reference.storage.reference
    private val imageRef = storageRef.child("images")

    init {
        user = getCurrentUser()
    }

    override fun isUserAuthenticatedInFirebase(): Boolean =
        auth.currentUser != null

    override fun getFirebaseAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser == null)
        }
        auth.addAuthStateListener(authStateListener)
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    override suspend fun signInWithEmailPassword(
        email: String,
        password: String
    ): Flow<Response<Boolean>> = flow {
        try {
            emit(Response.Loading)
            auth.signInWithEmailAndPassword(email, password).await()

            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override suspend fun signUpWithEmailPassword(
        email: String,
        password: String,
        name: String
    ) {
        try {
            auth.createUserWithEmailAndPassword(email, password)
            updateUserName(name)
//            emit(Response.Success(true))
        } catch (e: Exception) {
//            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override fun signOut(): Flow<Response<Boolean>> = flow {

        try {
            emit(Response.Loading)
            auth.signOut()
            emit(Response.Success(true))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }


    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override suspend fun sendResetPassword(email: String): Flow<Response<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserName(name: String) {
        val user = auth.currentUser
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        user!!.updateProfile(profileUpdates)
    }

    override suspend fun updateUserEmail(email: String) {
        val user = auth.currentUser
        user!!.updateEmail(email)
    }

    override suspend fun getProjects() = callbackFlow {
        val user = auth.currentUser
        val snapshotListener = db.collection("projects")
            .whereArrayContains("users", user?.email!!)
            .addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    val projects = snapshot.toObjects(Project::class.java)
                    Response.Success(projects)
                } else {
                    Response.Error(e?.message ?: e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addProject(projectName: String): Flow<Response<MessageBarState>> = flow {
        try {
            emit(Response.Loading)
            val date = LocalDateTime.now().toString()
            val projectsRef = db.collection("projects")
            val id = projectsRef.document().id
            val project = Project(
                id = id,
                name = projectName,
                users = listOf(user!!.email!!),
                creationDate = date
            )
            db.collection("projects")
                .document(id)
                .set(project)
                .await()
            emit(Response.Success(MessageBarState(message = "Dodano nowy projekt")))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override suspend fun updateProject(
        email: String,
        project: Project
    ): Flow<Response<MessageBarState>> = flow {
        try {
            Log.d("User", "started adding in Repo")
            emit(Response.Loading)
            val newUserList = project.users.plus(email)
            newUserList.forEach {
                Log.d("User", it)
            }
            val projectUpdate = Project(
                id = project.id,
                name = project.name,
                users = newUserList,
                creationDate = project.creationDate
            )
            db.collection("projects").document(project.id).set(projectUpdate).await()
            emit(Response.Success(MessageBarState(message = "Dodano nowego użytkownika")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
            emit(Response.Error(e.message ?: e.toString()))
        }

    }

    override suspend fun deleteProject(project: Project): Flow<Response<MessageBarState>> = flow {
        try {
            val projectsRef = db.collection("projects")
            emit(Response.Loading)
            projectsRef.document(project.id).delete().await()
            emit(Response.Success(MessageBarState(message = "Usunięto projekt")))
        } catch (e: Exception) {
            emit(Response.Error(e.message ?: e.toString()))
        }
    }

    override suspend fun getContractors(): Flow<Response<List<Contractor>>> = callbackFlow {
        val snapshotListener =
            db.collection("contractors").addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    Response.Loading
                    val contractors = snapshot.toObjects(Contractor::class.java)
                    Response.Success(contractors)
                } else {
                    Response.Error(e?.message ?: e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addContractor(
        name: String,
        contactPerson: String,
        phone: String,
        email: String
    ): Flow<Response<MessageBarState>> = flow {
        try {
            emit(Response.Loading)
            val projectsRef = db.collection("contractors")
            val id = projectsRef.document().id
            val contractor = Contractor(
                id = id,
                name = name,
                contactPerson = contactPerson,
                phone = phone,
                email = email
            )
            db.collection("contractors")
                .document(id)
                .set(contractor)
                .addOnSuccessListener {
                    Log.d("Main", "DocumentSnapshot written with ID: ${contractor.id}}")
                }
                .addOnFailureListener { e ->
                    Log.w("Main", "Error adding document", e)
                }
                .await()
            emit(Response.Success(MessageBarState(message = "Dodano nowy projekt")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun deleteContractor(contractor: Contractor): Flow<Response<MessageBarState>> =
        flow {
            try {
                val contractorRef =
                    db.collection("contractors")
                emit(Response.Loading)
                contractorRef.document(contractor.id).delete().await()
                emit(Response.Success(MessageBarState(message = "Usunięto podwykonawcę")))
            } catch (e: Exception) {
                emit(Response.ErrorMessageBar(MessageBarState(error = e)))
            }
        }

    override suspend fun addLocation(
        project: String,
        name: String
    ): Flow<Response<MessageBarState>> = flow {
        val locationsRef =
            db.collection("projects")
                .document(project)
                .collection("locations")
        try {
            emit(Response.Loading)
            val id = locationsRef.document().id
            val location = Location(id = id, name = name)
            locationsRef.document(id).set(location)
                .addOnSuccessListener {
                    Log.d("Main", "DocumentSnapshot written with ID: ${location.id}}")
                }
                .addOnFailureListener { e ->
                    Log.w("Main", "Error adding document", e)
                }
                .await()
            emit(Response.Success(MessageBarState(message = "Dodano lokalizację")))

        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun getLocations(project: String): Flow<Response<List<Location>>> =
        callbackFlow {
            val snapshotListener =
                db.collection("projects")
                    .document(project)
                    .collection("locations")
                    .addSnapshotListener { snapshot, e ->
                        val response = if (snapshot != null) {
                            Response.Loading
                            val locations = snapshot.toObjects(Location::class.java)
                            Response.Success(locations)

                        } else {
                            Log.d("Location", "Error: ${e?.message}")
                            Response.Error(e?.message ?: e.toString())

                        }
                        trySend(response).isSuccess
                    }
            awaitClose {
                snapshotListener.remove()
            }
        }

    override suspend fun deleteLocation(
        location: Location,
        projectId: String
    ): Flow<Response<MessageBarState>> = flow {
        val locationRef = db.collection("projects")
            .document(projectId)
            .collection("locations")
        try {
            emit(Response.Loading)
            locationRef.document(location.id).delete().await()
            emit(Response.Success(MessageBarState(message = "Usunieto lokal: ${location.name}")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun getDefectsTypes(): Flow<Response<List<DefectType>>> = callbackFlow {
        val snapshotListener =
            db.collection("types")
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        Response.Loading
                        val locations = snapshot.toObjects(DefectType::class.java)
                        Response.Success(locations)
                    } else {
                        Log.d("Location", "Error: ${e?.message}")
                        Response.Error(e?.message ?: e.toString())
                    }
                    trySend(response).isSuccess
                }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addDefectType(defectType: DefectType): Flow<Response<MessageBarState>> =
        flow {
            val defectTypesRef =
                db.collection("types")
            try {
                emit(Response.Loading)
                val id = defectTypesRef.document().id
                val type = DefectType(id = id, name = defectType.name)
                defectTypesRef.document(id).set(type)
                    .addOnSuccessListener {
                        Log.d("Type", "DocumentSnapshot written with ID: ${type.id}}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Type", "Error adding document", e)
                    }
                    .await()
                emit(Response.Success(MessageBarState(message = "Dodano nowy typ usterki")))

            } catch (e: Exception) {
                emit(Response.ErrorMessageBar(MessageBarState(error = e)))
            }
        }

    override suspend fun addDefect(defect: Defect): Flow<Response<MessageBarState>> = flow {
        Log.d("Defect Repo", "Started")
        val defectsRef =
            db.collection("defects")
        try {
            emit(Response.Loading)
            val id = defectsRef.document().id
            defect.id = id
            defectsRef.document(id).set(defect)
                .addOnSuccessListener {
                    Log.d("Defect add", "DocumentSnapshot written with ID: ${defect.id}}")
                }
                .addOnFailureListener { e ->
                    Log.w("Defect add", "Error adding document", e)
                }
                .await()
            emit(Response.Success(MessageBarState(message = "Dodano nową usterkę")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun getDefects(project: Project): Flow<Response<List<Defect>>> = callbackFlow {
        val querySnapshot: Query =
            db.collection("defects").whereEqualTo("project", project.name)
        val snapshotListener = querySnapshot
            .addSnapshotListener { snapshot, e ->
                val response = if (snapshot != null) {
                    Response.Loading
                    val defects = snapshot.toObjects(Defect::class.java)
                    Response.Success(defects)
                } else {
                    Log.d("Defect List", "Error: ${e?.message}")
                    Response.Error(e?.message ?: e.toString())
                }
                trySend(response).isSuccess
            }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun updateDefectStatus(
        defect: Defect
    ): Flow<Response<MessageBarState>> = flow {
        val updatedDefect = Defect(
            id = defect.id,
            comment = defect.comment,
            photos = defect.photos,
            location = defect.location,
            checkType = defect.checkType,
            contractor = defect.contractor,
            project = defect.project,
            type = defect.type,
            date = defect.date,
            status = !defect.status
        )
        val defectRef = db.collection("defects").document(defect.id)
        try {
            emit(Response.Loading)
            defectRef.set(updatedDefect)
            emit(Response.Success(MessageBarState(message = "Zaktualizowano status")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun updateDefect(defect: Defect): Flow<Response<MessageBarState>> = flow {
        val defectRef = db.collection("defects")
            .document(defect.id)
        try {
            emit(Response.Loading)
            defectRef.set(defect)
            emit(Response.Success(MessageBarState(message = "Zmieniono usterkę")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }
    }

    override suspend fun uploadPhotos(photo: String): String {
        val uri = Uri.parse(photo)
        val currentImageRef = imageRef.child("${uri.lastPathSegment}")
        val uploadTask = currentImageRef.putFile(uri).continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            currentImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result
            } else {
                Log.d("Upload", "Failed")
            }
        }.await()

        return uploadTask.toString()
    }

    override suspend fun deleteDefect(defect: Defect): Flow<Response<MessageBarState>> = flow {
        val defectRef = db.collection("defects")
        try {
            emit(Response.Loading)
            defectRef.document(defect.id).delete().await()
            defect.photoNames.forEach { photo ->
                Log.d("Delete", photo)
                imageRef.child(photo).delete()
                    .addOnSuccessListener {
                        Log.d("Delete", "Success")
                    }.addOnFailureListener{ e->
                        Log.d("Delete", e.message.toString())
                    }
            }
            emit(Response.Success(MessageBarState(message = "Usunięto usterkę.")))
        } catch (e: Exception) {
            emit(Response.ErrorMessageBar(MessageBarState(error = e)))
        }

    }

    override suspend fun generatePDF(context: Context, file: File, defect: Defect) {
        val pageHeight = 1120
        val pageWidth = 792
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val myPage = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = myPage.canvas
        val title = Paint()
        val paint = Paint()
        paint.color = Color.RED
        val date = LocalDate.now().toString()
        val titleText = "Raport z dn.: $date"
        title.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        title.textSize = 25f
        title.color = Color.BLACK
        title.textAlign = Paint.Align.LEFT
        canvas.drawText(titleText, 40f, 60f, title)
        canvas.drawLine(2f,60f,790f,42f,paint)
        pdfDocument.finishPage(myPage)
        withContext(Dispatchers.Main){
            try {
                pdfDocument.writeTo(FileOutputStream(file))
            } catch (e: Exception) {

            }
        }

        pdfDocument.close()
    }
}

