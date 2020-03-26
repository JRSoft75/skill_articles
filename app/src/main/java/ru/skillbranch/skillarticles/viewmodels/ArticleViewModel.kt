package ru.skillbranch.skillarticles.viewmodels

import androidx.lifecycle.LiveData
import ru.skillbranch.skillarticles.data.ArticleData
import ru.skillbranch.skillarticles.data.ArticlePersonalInfo
import ru.skillbranch.skillarticles.data.repositories.ArticleRepository
import ru.skillbranch.skillarticles.extensions.data.toAppSettings
import ru.skillbranch.skillarticles.extensions.data.toArticlePersonalInfo
import ru.skillbranch.skillarticles.extensions.format

class ArticleViewModel(private val articleId:String) : BaseViewModel<ArticleState>(ArticleState()), IArticleViewModel {
private val repository = ArticleRepository
    init{
        //subscribe on mutable data
        subscribeOnDataSource(getArticleData()){ article, state->
            article ?: return@subscribeOnDataSource null
            state.copy(
                shareLink = article.shareLink,
                title = article.title,
                category = article.category,
                categoryIcon = article.categoryIcon,
                date = article.date.format()
            )
        }

        subscribeOnDataSource(getArticleContent()){ content, state ->
            content ?: return@subscribeOnDataSource null
            state.copy(
                isLoadingContent = false,
                content = content
            )
        }

        subscribeOnDataSource(getArticlePersonalInfo()){ info, state ->
            info ?: return@subscribeOnDataSource null
            state.copy(
                isBookmark = info.isBookmark,
                isLike = info.isLike
            )
        }

        subscribeOnDataSource(repository.getAppSettings()){ settings, state ->
            state.copy(
                isDarkMode = settings.isDarkMode,
                isBigText = settings.isBigText
            )
        }
    }

    //load from network
     override fun getArticleContent(): LiveData<List<Any>?>{
        return repository.loadArticleContent(articleId)
    }

    //load from db
    override fun getArticleData(): LiveData<ArticleData?>{
        return repository.getArticle(articleId)
    }

    //load from db
    override fun getArticlePersonalInfo(): LiveData<ArticlePersonalInfo?>{
        return repository.loadArticlePersonalInfo(articleId)
    }

    override fun handleUpText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = true))
    }

    override fun handleDownText() {
        repository.updateSettings(currentState.toAppSettings().copy(isBigText = false))
    }

    override fun handleNightMode() {
        val settings = currentState.toAppSettings()
        repository.updateSettings(settings.copy(isDarkMode = !settings.isDarkMode))
    }

    override fun handleLike() {
        val toggleLike = {
            val info = currentState.toArticlePersonalInfo()
            repository.updateArticlePersonalInfo(info.copy(isLike = !info.isLike))
        }

        toggleLike()

        val msg = if(currentState.isLike) Notify.TextMessage("MArk is liked")
        else{
            Notify.ActionMessage(
                "Don't like it anymore", // message
                "No, still like it", //action on snackbar
                toggleLike //handle
            )
        }

        notify(msg)
    }

    override fun handleBookmark() {
        val toggleBookmark = {
            val info = currentState.toArticlePersonalInfo()
            repository.updateArticlePersonalInfo(info.copy(isBookmark = !info.isBookmark))
        }

        toggleBookmark()

        val msg = if(currentState.isBookmark) Notify.TextMessage("Mark is Bookmark")
        else{
            Notify.ActionMessage(
                "Don't bookmark it anymore", // message
                "No, still bookmark it", //action on snackbar
                toggleBookmark //handle
            )
        }

        notify(msg)
    }

    override fun handleShare() {
        val msg = "Share is not implemented"
        notify(Notify.ErrorMessage(msg, "OK", null))
    }

    //session state
    override fun handleToggleMenu() {
        updateState { it.copy(isShowMenu = !it.isShowMenu) }
    }

    override fun handleSearchMode(isSearch: Boolean) {
        TODO("Not yet implemented")
    }

    override fun handleSearch(query: String?) {
        TODO("Not yet implemented")
    }

}

data class ArticleState(
    val isAuth: Boolean = false, // юзер авторизован
    val isLoadingContent: Boolean = true, //контент загружается
    val isLoadingReviews: Boolean = true, //отзывы загружаются
    val isLike: Boolean = false, //отмечено как лайк
    val isBookmark:Boolean = false,
    val isShowMenu:Boolean = false,
    val isBigText:Boolean = false,
    val isDarkMode:Boolean = false, //
    val isSearch:Boolean = false, //режим поиска
    val searchQuery:String? = null, // поисковый запрос
    val searchResults: List<Pair<Int, Int>> = emptyList(), //результваты поиска (стартовая и конечная позиции)
    val searchPosition: Int = 0, // текущая позиция найденного материала
    val shareLink:String? = null, // ссылка Share
    val title:String? = null, // заголовок статьи
    val category:String? = null, //категория
    val categoryIcon:Any? = null, // иконка категории
    val date:String? = null, //дата публикации
    val author:Any? = null, //автор статьи
    val poster:String? = null, //обложка статьи
    val content: List<Any> = emptyList(), //контент
    val reviews: List<Any> = emptyList() //комментарии

)