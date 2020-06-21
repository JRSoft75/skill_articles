package ru.skillbranch.skillarticles.viewmodels.article

import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.repositories.ArticleRepository
import ru.skillbranch.skillarticles.data.repositories.MarkdownElement
import ru.skillbranch.skillarticles.extensions.data.toAppSettings
import ru.skillbranch.skillarticles.extensions.data.toArticlePersonalInfo
import ru.skillbranch.skillarticles.extensions.format
import ru.skillbranch.skillarticles.extensions.indexesOf
import ru.skillbranch.skillarticles.data.repositories.clearContent
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.NavigationCommand
import ru.skillbranch.skillarticles.viewmodels.base.Notify

class ArticleViewModel(
    handle: SavedStateHandle,
    private val articleId: String
) :
    BaseViewModel<ArticleState>(
        handle,
        ArticleState()
    ),  IArticleViewModel {
    private val repository = ArticleRepository
    private var clearContent: String? = null
//    private var menuIsShown: Boolean = false

    init {
        //subscribe on mutable data
        subscribeOnDataSource(getArticleData()) { article, state ->
            article ?: return@subscribeOnDataSource null
            Log.e("ArticleViewModel","author: ${article.author}")
            state.copy(
                shareLink = article.shareLink,
                title = article.title,
                author = article.author,
                category = article.category,
                categoryIcon = article.categoryIcon,
                date = article.date.format()
            )
        }

        subscribeOnDataSource(getArticleContent()) { content, state ->
            content ?: return@subscribeOnDataSource null
            state.copy(
                isLoadingContent = false,
                content = content
            )
        }

        subscribeOnDataSource(getArticlePersonalInfo()) { info, state ->
            info ?: return@subscribeOnDataSource null
            state.copy(
                isBookmark = info.isBookmark,
                isLike = info.isLike

            )
        }

        //subscribe on settings
        subscribeOnDataSource(repository.getAppSettings()) { settings, state ->
            state.copy(
                isDarkMode = settings.isDarkMode,
                isBigText = settings.isBigText
            )
        }

        subscribeOnDataSource(repository.isAuth()){auth, state ->
            state.copy(isAuth = auth)
        }
    }

    //load from network
    override fun getArticleContent(): LiveData<List<MarkdownElement>?> {
        return repository.loadArticleContent(articleId)
    }

    //load from db
    override fun getArticleData(): LiveData<ArticleData?> {
        return repository.getArticle(articleId)
    }

    //load from db
    override fun getArticlePersonalInfo(): LiveData<ArticlePersonalInfo?> {
        return repository.loadArticlePersonalInfo(articleId)
    }

    //session state
    override fun handleToggleMenu() {
        updateState { state ->
            state.copy(isShowMenu = !state.isShowMenu)
        }
    }

    //app settings
    override fun handleNightMode() {
        val settings = currentState.toAppSettings()
        repository.updateSettings(settings.copy(isDarkMode = !settings.isDarkMode))
    }

    override fun handleUpText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = true))
    }

    override fun handleDownText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = false))
    }

    override fun handleCopyCode(){
        notify(Notify.TextMessage("Code copy to clipboard"))
    }

    override fun handleSendComment() {
        if(!currentState.isAuth) navigate(NavigationCommand.StartLogin())
        //TODO sens command
    }

    override fun handleLike() {
        val isLiked = currentState.isLike
        val toggleLike = {
            val info = currentState.toArticlePersonalInfo()
            repository.updateArticlePersonalInfo(info.copy(isLike = !info.isLike))
        }

        toggleLike()

        val msg = if (currentState.isLike) Notify.TextMessage("Mark is liked")
        else {
            Notify.ActionMessage(
                "Don`t like it anymore", // message
                "No, still like it", //action on snackbar
                toggleLike //handle
            )
        }

        notify(msg)
    }

    override fun handleBookmark() {
        val info = currentState.toArticlePersonalInfo()
        repository.updateArticlePersonalInfo(info.copy(isBookmark = !info.isBookmark))

        val msg = if (currentState.isBookmark) "Add to bookmarks" else "Remove from bookmarks"
        notify(Notify.TextMessage(msg))
    }

    override fun handleShare() {
        val msg = "Share is not implemented"
        notify(Notify.ErrorMessage(msg, "OK", null))
    }

    override fun handleSearchMode(isSearch: Boolean) {
        updateState { it.copy(isSearch = isSearch, isShowMenu = false,searchPosition = 0) }
    }

    override fun handleSearch(query: String?) {
        query ?: return
        if(clearContent ==null && currentState.content.isNotEmpty()) clearContent = currentState.content.clearContent()
        val result = clearContent
            .indexesOf(query)
            .map{it to it + query.length}
        updateState { it.copy(searchQuery = query, searchResults = result, searchPosition = 0) }
    }

    override fun handleUpResult(){
        updateState { it.copy(searchPosition = it.searchPosition.dec()) }
    }

    override fun handleDownResult(){
        updateState { it.copy(searchPosition = it.searchPosition.inc()) }
    }

//    fun hideMenu() {
//        updateState { it.copy(isShowMenu = false) }
//    }
//
//    fun showMenu() {
//        updateState { it.copy(isShowMenu = menuIsShown) }
//    }

//    fun handleSearchQuery(query: String) {
//        updateState { it.copy(searchQuery = query) }
//    }
//
//    fun handleIsSearch(isSearch: Boolean) {
//        updateState { it.copy(isSearch = isSearch) }
//    }

}

data class ArticleState(
    val isAuth: Boolean = false, // юзер авторизован
    val isLoadingContent: Boolean = true, //контент загружается
    val isLoadingReviews: Boolean = true, //отзывы загружаются
    val isLike: Boolean = false, //отмечено как лайк
    val isBookmark: Boolean = false,
    val isShowMenu: Boolean = false,
    val isBigText: Boolean = false,
    val isDarkMode: Boolean = false, //
    val isSearch: Boolean = false, //режим поиска
    val searchQuery: String? = null, // поисковый запрос
    val searchResults: List<Pair<Int, Int>> = emptyList(), //результваты поиска (стартовая и конечная позиции)
    val searchPosition: Int = 0, // текущая позиция найденного материала
    val shareLink: String? = null, // ссылка Share
    val title: String? = null, // заголовок статьи
    val category: String? = null, //категория
    val categoryIcon: Any? = null, // иконка категории
    val date: String? = null, //дата публикации
    val author: Any? = null, //автор статьи
    val poster: String? = null, //обложка статьи
    val content: List<MarkdownElement> = emptyList(), //контент
    val reviews: List<Any> = emptyList() //комментарии
) : IViewModelState {
    override fun save(outState: SavedStateHandle){
        outState.set("isSearch",isSearch)
        outState.set("searchQuery", searchQuery)
        outState.set("searchResults", searchResults)
        outState.set("searchPosition", searchPosition)
    }

    override fun restore(savedState: SavedStateHandle): ArticleState {
        return copy(
            isSearch = savedState["isSearch"] ?: false,
            searchQuery = savedState["searchQuery"] ,
            searchResults = savedState["searchResults"] ?: emptyList(),
            searchPosition = savedState["searchPosition"] ?: 0
        )
    }
}