package com.example.psassignment.di

import android.content.Context
import com.example.psassignment.data.repository.AssetDataRepository
import com.example.psassignment.domain.algorithm.GreedyAlgorithm
import com.example.psassignment.domain.algorithm.HungarianAlgorithm
import com.example.psassignment.domain.algorithm.RoutingAlgorithm
import com.example.psassignment.domain.repository.DataRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRoutingAlgorithm(): RoutingAlgorithm {
        return GreedyAlgorithm()            // The fast/approx answer
//        return HungarianAlgorithm()       // More complicated, but the best answer
    }

    @Provides
    @Singleton
    fun provideDataRepository(
        @ApplicationContext context: Context,
        gson: Gson
    ): DataRepository {
        return AssetDataRepository(context, gson)
    }
}