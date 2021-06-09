package com.sun.unsplash_02

import com.sun.unsplash_02.data.model.Collection
import com.sun.unsplash_02.data.source.ImageRepository
import com.sun.unsplash_02.data.source.remote.OnFetchDataJsonListener
import com.sun.unsplash_02.screen.home.HomeContract
import com.sun.unsplash_02.screen.home.HomePresenter
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@Suppress("UNCHECKED_CAST")
@RunWith(MockitoJUnitRunner::class)
class HomePresenterTest {

    @Mock
    private lateinit var view: HomeContract.View

    @Mock
    private lateinit var repository: ImageRepository

    @Mock
    private lateinit var exception: Exception

    private lateinit var presenter: HomePresenter
    private lateinit var listCollections: MutableList<Collection>

    @Before
    fun setUp() {
        presenter = HomePresenter(repository).apply { setView(view) }
        listCollections = FakeData.COLLECTIONS
    }

    @Test
    fun `get collections success`() {
        `when`(
            repository.getCollections(any())
        ).thenAnswer {
            (it.arguments[0] as OnFetchDataJsonListener<MutableList<Collection>>).onSuccess(
                listCollections
            )
        }
        presenter.loadListCollections()
        verify(view).onCollectionLoaded(listCollections)
    }

    @Test
    fun `get collections error`() {
        `when`(
            repository.getCollections(any())
        ).thenAnswer {
            (it.arguments[0] as OnFetchDataJsonListener<MutableList<Collection>>).onError(exception)
        }
        presenter.loadListCollections()
        verify(view).onError(exception)
    }
}
