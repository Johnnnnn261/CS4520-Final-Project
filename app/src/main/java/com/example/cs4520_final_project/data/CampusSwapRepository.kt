package com.example.cs4520_final_project.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

// Handles all Firebase Auth and Firestore work for CampusSwap.
// Screens should call this file instead of talking to Firebase directly.
object CampusSwapRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signInAnonymouslyIfNeeded(): String {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            return currentUser.uid
        }

        val result = auth.signInAnonymously().await()
        return result.user?.uid ?: ""
    }

    fun getCurrentUserId(): String {
        return auth.currentUser?.uid ?: ""
    }

    suspend fun saveProfile(profileInfo: ProfileInfo) {
        val uid = signInAnonymouslyIfNeeded()

        db.collection("users")
            .document(uid)
            .set(profileInfo, SetOptions.merge())
            .await()
    }

    suspend fun loadProfile(): ProfileInfo? {
        val uid = signInAnonymouslyIfNeeded()

        val snapshot = db.collection("users")
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(ProfileInfo::class.java)
    }

    suspend fun saveListing(listingInfo: ListingInfo): ListingInfo {
        val uid = signInAnonymouslyIfNeeded()

        val listingToSave = listingInfo.copy(
            sellerId = uid,
            createdAt = System.currentTimeMillis()
        )

        db.collection("items")
            .document(listingToSave.id.toString())
            .set(listingToSave)
            .await()

        return listingToSave
    }

    suspend fun updateListing(listingInfo: ListingInfo) {
        db.collection("items")
            .document(listingInfo.id.toString())
            .set(listingInfo, SetOptions.merge())
            .await()
    }

    suspend fun loadAllListings(): List<ListingInfo> {
        val snapshot = db.collection("items")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(ListingInfo::class.java)
        }
    }

    suspend fun saveComment(commentInfo: CommentInfo) {
        db.collection("items")
            .document(commentInfo.listingId.toString())
            .collection("comments")
            .document(commentInfo.id.toString())
            .set(commentInfo)
            .await()
    }

    suspend fun loadCommentsForListing(listingId: Long): List<CommentInfo> {
        val snapshot = db.collection("items")
            .document(listingId.toString())
            .collection("comments")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .get()
            .await()

        return snapshot.documents.mapNotNull { document ->
            document.toObject(CommentInfo::class.java)
        }
    }

}
