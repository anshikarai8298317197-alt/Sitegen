package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects ORDER BY lastModified DESC")
    fun getAllProjectsFlow(): Flow<List<ProjectEntity>>

    @Query("SELECT * FROM projects ORDER BY lastModified DESC")
    suspend fun getAllProjects(): List<ProjectEntity>

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    suspend fun getProjectById(id: String): ProjectEntity?

    @Query("SELECT * FROM projects WHERE id = :id LIMIT 1")
    fun getProjectByIdFlow(id: String): Flow<ProjectEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: String)
}

@Dao
interface BlogDao {
    @Query("SELECT * FROM blog_posts WHERE projectId = :projectId ORDER BY createdAt DESC")
    fun getPostsByProjectFlow(projectId: String): Flow<List<BlogPostEntity>>

    @Query("SELECT * FROM blog_posts ORDER BY createdAt DESC")
    fun getAllPostsFlow(): Flow<List<BlogPostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: BlogPostEntity)

    @Query("DELETE FROM blog_posts WHERE id = :id")
    suspend fun deletePost(id: String)
}

@Dao
interface FormDao {
    @Query("SELECT * FROM form_submissions ORDER BY submittedAt DESC")
    fun getAllSubmissionsFlow(): Flow<List<FormSubmissionEntity>>

    @Query("SELECT * FROM form_submissions WHERE projectId = :projectId ORDER BY submittedAt DESC")
    fun getSubmissionsByProjectFlow(projectId: String): Flow<List<FormSubmissionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: FormSubmissionEntity)

    @Query("UPDATE form_submissions SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)

    @Query("DELETE FROM form_submissions WHERE id = :id")
    suspend fun deleteSubmission(id: String)
}

@Dao
interface ProductDao {
    @Query("SELECT * FROM ecommerce_products WHERE projectId = :projectId")
    fun getProductsByProjectFlow(projectId: String): Flow<List<EcommerceProductEntity>>

    @Query("SELECT * FROM ecommerce_products")
    fun getAllProductsFlow(): Flow<List<EcommerceProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: EcommerceProductEntity)

    @Query("DELETE FROM ecommerce_products WHERE id = :id")
    suspend fun deleteProduct(id: String)
}

@Dao
interface CreditDao {
    @Query("SELECT * FROM credit_transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<CreditTransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: CreditTransactionEntity)
}

@Dao
interface SettingsDao {
    @Query("SELECT * FROM system_settings WHERE id = 1 LIMIT 1")
    fun getSettingsFlow(): Flow<SystemSettingsEntity?>

    @Query("SELECT * FROM system_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettings(): SystemSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(settings: SystemSettingsEntity)
}

@Dao
interface DeploymentDao {
    @Query("SELECT * FROM deployment_snapshots WHERE projectId = :projectId ORDER BY versionNumber DESC")
    fun getSnapshotsFlow(projectId: String): Flow<List<DeploymentSnapshotEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSnapshot(snapshot: DeploymentSnapshotEntity)
}
