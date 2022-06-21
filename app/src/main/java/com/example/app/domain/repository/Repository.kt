package com.example.app.domain.repository

import android.content.Context
import com.example.app.domain.model.*
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import java.io.File


interface Repository {

    fun isUserAuthenticatedInFirebase(): Boolean

    fun getFirebaseAuthState(): Flow<Boolean>

    suspend fun signInWithEmailPassword(email:String , password:String): Flow<Response<Boolean>>

    suspend fun signUpWithEmailPassword(email: String , password: String, name: String)

    fun signOut() : Flow<Response<Boolean>>

    fun getCurrentUser() : FirebaseUser?

    suspend fun sendResetPassword(email : String) : Flow<Response<Boolean>>

    suspend fun updateUserName(name:String)

    suspend fun updateUserEmail(email:String)

    suspend fun getProjects(): Flow<Response<List<Project>>>

    suspend fun addProject(projectName: String): Flow<Response<MessageBarState>>

    suspend fun updateProject(email: String, project: Project): Flow<Response<MessageBarState>>

    suspend fun deleteProject(project: Project): Flow<Response<MessageBarState>>

    suspend fun getContractors(): Flow<Response<List<Contractor>>>

    suspend fun addContractor(name: String, contactPerson: String, phone:String, email: String): Flow<Response<MessageBarState>>

    suspend fun deleteContractor(contractor: Contractor): Flow<Response<MessageBarState>>

    suspend fun addLocation(project: String, name: String): Flow<Response<MessageBarState>>

    suspend fun getLocations(project: String): Flow<Response<List<Location>>>

    suspend fun deleteLocation(location: Location, projectId:String): Flow<Response<MessageBarState>>

    suspend fun getDefectsTypes(): Flow<Response<List<DefectType>>>

    suspend fun addDefectType(defectType: DefectType): Flow<Response<MessageBarState>>

    suspend fun addDefect(defect: Defect, ): Flow<Response<MessageBarState>>

    suspend fun getDefects(project: Project): Flow<Response<List<Defect>>>

    suspend fun updateDefectStatus(defect: Defect): Flow<Response<MessageBarState>>

    suspend fun updateDefect(defect: Defect): Flow<Response<MessageBarState>>

    suspend fun uploadPhotos(photo: String): String

    suspend fun deleteDefect(defect: Defect): Flow<Response<MessageBarState>>

    suspend fun generatePDF(context: Context, file: File, defect: Defect)





}