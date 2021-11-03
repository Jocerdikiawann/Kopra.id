package com.trian.data.repository

import android.graphics.Bitmap
import com.google.firebase.firestore.Query
import com.trian.data.remote.FirestoreSource
import com.trian.domain.models.Product
import com.trian.domain.models.Store
import com.trian.domain.models.network.GetStatus
import com.trian.domain.models.toUpdatedData
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class StoreRepositoryImpl(
    private val source: FirestoreSource
):StoreRepository {

    override suspend fun getListProductByStore(storeId: String): GetStatus<List<Product>> {
       return try {
           val result = source.productCollection()
               .whereEqualTo("storeUid",storeId)
               .orderBy("createdAt",Query.Direction.ASCENDING)
               .get()
               .await()
           val products = result.documents.map {
               it.toObject(Product::class.java)!!
           }
           GetStatus.HasData(products)
       }catch (e:Exception){
           GetStatus.NoData(e.message!!)
       }

    }

    override suspend fun getDetailProduct(productId: String): GetStatus<Product> {
        return try {
            val product = source.productCollection().document(productId).get().await().toObject(Product::class.java)
            GetStatus.HasData(product)
        }catch (e:Exception){
            GetStatus.NoData(e.message!!)
        }
    }

    override suspend fun getListStore(): GetStatus<List<Store>> {
        return try {
            val result = source.storeCollection().orderBy("createdAt",Query.Direction.ASCENDING)
                .get()
                .await()
            val transform = result.documents.map {
                it.toObject(Store::class.java)!!
            }
            GetStatus.HasData(transform)
        }catch (e:Exception){
            GetStatus.NoData(e.message!!)
        }
    }

    override suspend fun getDetailStore(storeId: String): GetStatus<Store> {
        return try {
            val result = source.storeCollection().document(storeId)
                .get()
                .await().toObject(Store::class.java)
            GetStatus.HasData(result)
        }catch (e:Exception){
            GetStatus.NoData(e.message!!)
        }
    }

    override fun createProduct(
        product: Product,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
        val id = source.productCollection().id
        product.apply {
            uid = id
        }
        source.productCollection()
            .document(id)
            .set(product)
            .addOnCompleteListener {
                onComplete(true,"")
            }
            .addOnFailureListener {
                onComplete(false,"")
            }
    }

    override fun updateProduct(
        product: Product,
        onComplete: (success: Boolean, message: String) -> Unit
    ) {
       source.productCollection().document(product.uid)
           .update(product.toUpdatedData())
           .addOnCompleteListener {onComplete(true,"")  }
           .addOnFailureListener { onComplete(false,it.message!!) }
    }

    override fun createStore(store: Store, onComplete: (success: Boolean, url: String) -> Unit) {
      source.firebaseAuth.currentUser?.let {

          store.apply {
              uid = it.uid
              tenantUid = it.uid
          }

          source.storeCollection().document(it.uid)
              .set(store)
              .addOnCompleteListener {
                  onComplete(true,"")
              }.addOnFailureListener {
                  onComplete(false,it.message!!)
              }
      }
    }

    override fun updateStore(store: Store, onComplete: (success: Boolean, url: String) -> Unit) {
        source.storeCollection().document(store.uid)
            .update(store.toUpdatedData())
            .addOnCompleteListener {
                onComplete(true,"")
            }.addOnFailureListener {
                onComplete(false,"")
            }
    }

    override fun uploadBanner(bitmap: Bitmap, onComplete: (success: Boolean, url: String) -> Unit) {
        source.firebaseAuth.currentUser?.let {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageReference = source
                .storageStore()
                .child(it.uid)

            storageReference.putBytes(data)
                .continueWith {
                        task->
                    if(!task.isSuccessful){
                        task.exception?.let {
                            throw  it
                        }
                    }
                    storageReference.downloadUrl

                }
                .addOnSuccessListener {
                        task->
                    if(task.isComplete){
                        onComplete(true,task.result.toString())
                    }
                }.addOnFailureListener {
                    onComplete(false,"")
                }
        }?:onComplete(false,"")
    }

    override fun uploadLogo(bitmap: Bitmap, onComplete: (success: Boolean, url: String) -> Unit) {
        source.firebaseAuth.currentUser?.let {
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            val storageReference = source
                .storageStore()
                .child("LOGO-${it.uid}")

            storageReference.putBytes(data)
                .continueWith {
                        task->
                    if(!task.isSuccessful){
                        task.exception?.let {
                            throw  it
                        }
                    }
                    storageReference.downloadUrl

                }
                .addOnSuccessListener {
                        task->
                    if(task.isComplete){
                        onComplete(true,task.result.toString())
                    }
                }.addOnFailureListener {
                    onComplete(false,"")
                }
        }?:onComplete(false,"")

    }

}