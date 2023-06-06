package com.padicare.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.padicare.model.CommentItem
import com.padicare.model.PostItem
import com.padicare.repository.ApiServices

class CommentPagingSource(private val apiServices: ApiServices, private val id: String,private val token: String) : PagingSource<Int, CommentItem>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, CommentItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CommentItem> {
        try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiServices.getComments(id, page = page, size = 5, authorization = "Bearer $token").listComment

            return LoadResult.Page(
                data = responseData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else page + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }
}