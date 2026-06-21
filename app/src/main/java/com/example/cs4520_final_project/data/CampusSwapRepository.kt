package com.example.cs4520_final_project.data

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlin.jvm.java

// Handles all Firebase Auth and Firestore work for CampusSwap.
// Screens should call this file instead of talking to Firebase directly.
class CampusSwapRepository(private val uid: String) {
    private val db = FirebaseFirestore.getInstance()


    // Profile
    suspend fun saveProfile(profileInfo: ProfileInfo) {

        db.collection("users").document(uid).set(profileInfo, SetOptions.merge()).await()
    }

    suspend fun loadProfile(): ProfileInfo? {

        val snapshot = db.collection("users").document(uid).get().await()

        return snapshot.toObject(ProfileInfo::class.java)
    }


    // Listings
    private val listingCollection = Firebase.firestore.collection("items")

    suspend fun saveListing(listingInfo: ListingInfo): ListingInfo {

        val listingToSave = listingInfo.copy(
            sellerId = uid, createdAt = System.currentTimeMillis()
        )

        listingCollection.document(listingToSave.id.toString()).set(listingToSave).await()

        return listingToSave
    }

    suspend fun updateListing(listingInfo: ListingInfo) {
        listingCollection.document(listingInfo.id.toString()).set(listingInfo, SetOptions.merge())
            .await()
    }


    fun observeListings(): Flow<List<ListingInfo>> = callbackFlow {
        val registration = listingCollection.orderBy(
            "createdAt", Query.Direction.DESCENDING
        ).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val listings = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(ListingInfo::class.java)
            }.orEmpty()
            trySend(listings)
        }
        awaitClose { registration.remove() }
    }


    // Comments
    suspend fun saveComment(commentInfo: CommentInfo) {
        listingCollection.document(commentInfo.listingId.toString()).collection("comments")
            .document(commentInfo.id.toString()).set(commentInfo).await()
    }


    fun observeComments(listingId: Long): Flow<List<CommentInfo>> = callbackFlow {
        val registration =
            db.collection("items").document(listingId.toString()).collection("comments")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val comments = snapshot?.documents?.mapNotNull { doc ->
                        doc.toObject(CommentInfo::class.java)
                    }.orEmpty()

                    trySend(comments)
                }
        awaitClose { registration.remove() }
    }


}
