package com.tonyk.android.movieo.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class FirebaseRepository @Inject constructor(){

    private val database = Firebase.database.reference
    fun getMoviesFromFirebase(): Flow<List<String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moviesList = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val movie = childSnapshot.getValue(String::class.java)
                    movie?.let { moviesList.add(it) }
                }
                trySend(moviesList)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val moviesNode = database.child("movies")
        moviesNode.addValueEventListener(listener)

        awaitClose {
            moviesNode.removeEventListener(listener)
        }
    }
    fun getNewMoviesFromFirebase(): Flow<List<String>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val moviesList = mutableListOf<String>()
                for (childSnapshot in snapshot.children) {
                    val movie = childSnapshot.getValue(String::class.java)
                    movie?.let { moviesList.add(it) }
                }
                trySend(moviesList)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val moviesNode = database.child("newmovies")
        moviesNode.addValueEventListener(listener)

        awaitClose {
            moviesNode.removeEventListener(listener)
        }
    }

    fun getFeaturedMovieFromFirebase(): Flow<String?> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val featuredMovie = snapshot.getValue(String::class.java)
                trySend(featuredMovie)
            }
            override fun onCancelled(error: DatabaseError) {
                close(error.toException())
            }
        }
        val featuredNode = database.child("featured")
        featuredNode.addValueEventListener(listener)

        awaitClose {
            featuredNode.removeEventListener(listener)
        }
    }
}