package com.indie.apps.newyorktimestories.usecase

import com.indie.apps.newyorktimestories.data.model.toUIArticle
import com.indie.apps.newyorktimestories.data.repository.StoryRepository
import com.indie.apps.newyorktimestories.ui.model.UIArticle
import com.indie.apps.newyorktimestories.util.ErrorMessage
import com.indie.apps.newyorktimestories.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.io.IOException

class SearchStoriesFromSectionUseCase(
    private val storyRepository: StoryRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun loadData(search: String, section: String): Flow<Resource<List<UIArticle>>> {
        return storyRepository
            .searchRecordsFromSection(search, section)
            .map { item ->

                when(item){
                    is Resource.Error -> {
                        Resource.Error(item.message ?: ErrorMessage.NO_MESSAGE.message)
                        // emit(Resource.Error(item.message ?: ErrorMessage.NO_MESSAGE.message))
                    }
                    is Resource.Loading -> {
                        Resource.Loading()
                        // emit(Resource.Loading())
                    }
                    is Resource.Success -> {
                        if(item.data != null)
                        {
                            Resource.Success(item.data.map { item1 -> item1.toUIArticle() })
                            //emit(Resource.Success(item.data.map { item1 -> item1.toUIArticle() }))
                        }else{
                            Resource.Error(ErrorMessage.N0_DATA_FOUND.message)
                            //emit(Resource.Error(ErrorMessage.N0_DATA_FOUND.message))
                        }
                    }
                }
            }.catch { error ->
                when(error) {
                    is IOException -> emit(Resource.Error(ErrorMessage.NO_NETWORK.message))
                    else -> emit(Resource.Error(error.localizedMessage))
                }
            }.flowOn(dispatcher)
    }

}