package es.avifer.collapsiblelist

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused", "WeakerAccess")
class CollapsibleList(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    companion object {

        private var DEFAULT_BORDER_COLOR_LIST = R.color.default_background_header
        private const val DEFAULT_SIZE_BORDER_LIST = 4
        private const val DEFAULT_CORNER_BORDER_LIST = 0

        private const val DEFAULT_CORNER_RADIUS_HEADER = 0
        private const val DEFAULT_HEIGHT_HEADER = 60
        private var DEFAULT_BACKGROUND_HEADER = R.color.default_background_header

        private var DEFAULT_TEXT_TITLE_HEADER = ""
        private var DEFAULT_COLOR_TEXT_TITLE_HEADER = R.color.default_text_color_header
        private var DEFAULT_SIZE_TEXT_TITLE_HEADER = 12
        private var DEFAULT_MAX_LINES_TITLE_HEADER = 2
        private var DEFAULT_PADDING_HORIZONTAL_TEXT_TITLE_HEADER = 16
        private var DEFAULT_PADDING_VERTICAL_TEXT_TITLE_HEADER = 0

    }

    //VIEWS
    private var constraintLayoutParent: ConstraintLayout? = null

    private var backgroundHeader: View? = null
    private var titleHeader: TextView? = null
    private var btnCollapsibleHeader: ImageView? = null

    private var listElements: RecyclerView? = null

    //VARS CONFIG
    private var listCollapsed = false

    //ATTRIBUTES
    private var attributes: TypedArray? = null

    private val actionOnClickButtonCollapsible = {
        if (listCollapsed) {
            btnCollapsibleHeader?.setImageResource(R.drawable.icon_expanded_more)
            listElements?.visibility = VISIBLE

        } else {
            btnCollapsibleHeader?.setImageResource(R.drawable.icon_expanded_less)
            listElements?.visibility = GONE
        }
        listCollapsed = !listCollapsed
    }

    init {
        inflate(context, R.layout.view__collapsible_list, this)
        this.attributes = context.obtainStyledAttributes(attributeSet, R.styleable.CollapsibleList)
        initViewHeader()
        initParent()
        initTitleHeader()
        initButtonCollapsibleHeader()
        initListElements()
    }

    //region INIT PARENT

    private fun initParent() {
        constraintLayoutParent = findViewById(R.id.view__collapsible_list)
        initColorBorderList()
        initSizeBorderList()
        initCornerBorderList()
    }

    private fun initColorBorderList() {
        val colorBorderListAttribute =
            this.attributes?.getColor(
                R.styleable.CollapsibleList_colorBorderList,
                ContextCompat.getColor(context, DEFAULT_BORDER_COLOR_LIST)
            ) ?: ContextCompat.getColor(context, DEFAULT_BORDER_COLOR_LIST)

        setColorBorderList(colorBorderListAttribute)
    }

    private fun initSizeBorderList() {
        val sizeBorderListAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_sizeBorderList,
                DEFAULT_SIZE_BORDER_LIST
            ) ?: DEFAULT_SIZE_BORDER_LIST

        setSizeBorderList(sizeBorderListAttribute)
    }

    private fun initCornerBorderList() {
        val sizeCornerBorderListAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_cornerBorderRadius,
                DEFAULT_CORNER_BORDER_LIST
            ) ?: DEFAULT_CORNER_BORDER_LIST

        setCornerBorderList(sizeCornerBorderListAttribute)
    }

    //endregion

    //region INIT HEADER

    private fun initViewHeader() {
        backgroundHeader = findViewById(R.id.collapsible_list__view__background_header)
        setOnClickHeader()
        initHeightHeader()
        initCornerRadiusHeader()
        initBackgroundColorHeader()
    }

    private fun setOnClickHeader() {
        backgroundHeader?.setOnClickListener { actionOnClickButtonCollapsible() }
    }

    private fun initHeightHeader() {
        val heightAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_heightHeader,
                DEFAULT_HEIGHT_HEADER
            ) ?: DEFAULT_HEIGHT_HEADER

        setHeightHeader(heightAttribute)
    }

    private fun initCornerRadiusHeader() {
        val cornerRadiusAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_cornerRadiusHeader,
                DEFAULT_CORNER_RADIUS_HEADER
            ) ?: DEFAULT_CORNER_RADIUS_HEADER

        setCornerRadiusHeader(cornerRadiusAttribute)
    }

    private fun initBackgroundColorHeader() {
        val backgroundHeaderAttribute =
            this.attributes?.getColor(
                R.styleable.CollapsibleList_backgroundColorHeader,
                ContextCompat.getColor(context, DEFAULT_BACKGROUND_HEADER)
            ) ?: ContextCompat.getColor(context, DEFAULT_BACKGROUND_HEADER)

        setBackgroundColorHeader(backgroundHeaderAttribute)
    }

    //endregion

    //region INIT TEXT HEADER

    private fun initTitleHeader() {
        titleHeader = findViewById(R.id.collapsible_list__label__title_header)
        initTextTitleHeader()
        initColorTextTitleHeader()
        initSizeTextTitleHeader()
        initMaxLinesTextTitleHeader()
        initPaddingHorizontalTextTitleHeader()
        initPaddingVerticalTextTitleHeader()
    }

    private fun initTextTitleHeader() {
        val textTitleHeaderAttribute =
            this.attributes?.getString(R.styleable.CollapsibleList_textHeader)
                ?: DEFAULT_TEXT_TITLE_HEADER

        setTextTitleHeader(textTitleHeaderAttribute)
    }

    private fun initColorTextTitleHeader() {
        val colorTextTitleHeaderAttribute =
            this.attributes?.getColor(
                R.styleable.CollapsibleList_colorTextHeader,
                ContextCompat.getColor(context, DEFAULT_COLOR_TEXT_TITLE_HEADER)
            ) ?: ContextCompat.getColor(context, DEFAULT_COLOR_TEXT_TITLE_HEADER)

        setColorTextTitleHeader(colorTextTitleHeaderAttribute)
    }

    private fun initSizeTextTitleHeader() {
        val sizeTextTitleHeaderAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_sizeTextHeader,
                DEFAULT_SIZE_TEXT_TITLE_HEADER
            ) ?: DEFAULT_SIZE_TEXT_TITLE_HEADER

        setSizeTextTitleHeader(sizeTextTitleHeaderAttribute)
    }

    private fun initMaxLinesTextTitleHeader() {
        val maxLinesTextTitleHeaderAttribute =
            this.attributes?.getInt(
                R.styleable.CollapsibleList_maxLinesTextHeader,
                DEFAULT_MAX_LINES_TITLE_HEADER
            ) ?: DEFAULT_MAX_LINES_TITLE_HEADER

        setMaxLinesTextTitleHeader(maxLinesTextTitleHeaderAttribute)
    }

    private fun initPaddingHorizontalTextTitleHeader() {
        val paddingHorizontalTextTitleHeaderAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_paddingHorizontalTextHeader,
                DEFAULT_PADDING_HORIZONTAL_TEXT_TITLE_HEADER
            ) ?: DEFAULT_PADDING_HORIZONTAL_TEXT_TITLE_HEADER

        setPaddingHorizontalTextTitleHeader(paddingHorizontalTextTitleHeaderAttribute)
    }

    private fun initPaddingVerticalTextTitleHeader() {
        val paddingVerticalTextTitleHeaderAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_paddingVerticalTextHeader,
                DEFAULT_PADDING_VERTICAL_TEXT_TITLE_HEADER
            ) ?: DEFAULT_PADDING_VERTICAL_TEXT_TITLE_HEADER

        setPaddingVerticalTextTitleHeader(paddingVerticalTextTitleHeaderAttribute)
    }

    //endregion

    //region INIT BUTTON HEADER

    private fun initButtonCollapsibleHeader() {
        btnCollapsibleHeader = findViewById(R.id.collapsible_list__img__img_collapsible)
    }

    //endregion

    //region INIT LIST ELEMENTS

    private fun initListElements() {
        listElements = findViewById(R.id.collapsible_list__list__list_elements)
        initLayoutManagerList()
    }

    private fun initLayoutManagerList() {
        listElements?.let {
            with(it) {
                layoutManager = object : LinearLayoutManager(context) {
                    override fun canScrollVertically() = false
                }
            }
        }
    }

    //endregion

    //region MODIFIERS HEADER

    private fun setHeightHeader(heightHeader: Int) {
        backgroundHeader?.layoutParams?.height = heightHeader
    }

    fun setCornerRadiusHeader(cornerRadius: Int) {
        (backgroundHeader?.background as? GradientDrawable)?.cornerRadius = cornerRadius.toFloat()
    }

    fun setBackgroundColorHeader(color: Int) {
        (backgroundHeader?.background as? GradientDrawable)?.color = ColorStateList.valueOf(color)
    }

    //endregion

    //region MODIFIERS TEXT HEADER

    fun setTextTitleHeader(textTitle: String) {
        titleHeader?.text = textTitle
    }

    fun setColorTextTitleHeader(@ColorInt colorTextTitleHeader: Int) {
        titleHeader?.setTextColor(colorTextTitleHeader)
    }

    fun setSizeTextTitleHeader(sizeTextTitleHeader: Int) {
        titleHeader?.textSize = sizeTextTitleHeader.toFloat()
    }

    fun setMaxLinesTextTitleHeader(maxLinesTextTitleHeader: Int) {
        titleHeader?.maxLines = maxLinesTextTitleHeader
    }

    fun setPaddingHorizontalTextTitleHeader(paddingHorizontal: Int) {
        val paddingTop = titleHeader?.paddingTop ?: 0
        val paddingBottom = titleHeader?.paddingBottom ?: 0

        titleHeader?.setPadding(
            paddingHorizontal,
            paddingTop,
            paddingHorizontal,
            paddingBottom
        )
    }

    fun setPaddingVerticalTextTitleHeader(paddingVertical: Int) {
        val paddingStart = titleHeader?.paddingStart ?: 0
        val paddingEnd = titleHeader?.paddingEnd ?: 0

        titleHeader?.setPadding(
            paddingStart,
            paddingVertical,
            paddingEnd,
            paddingVertical
        )
    }

    //endregion

    //region MODIFIERS LIST ELEMENTS

    fun <T> setAdapter(adapterList: CollapsibleListAdapter<T>) {
        listElements?.adapter = adapterList
    }

    //endregion

    //region MODIFIERS PARENT

    fun setColorBorderList(colorBorderList: Int) {
        val sizeBorderListAttribute =
            this.attributes?.getLayoutDimension(
                R.styleable.CollapsibleList_sizeBorderList,
                DEFAULT_SIZE_BORDER_LIST
            ) ?: DEFAULT_SIZE_BORDER_LIST

        (constraintLayoutParent?.background as? GradientDrawable)?.setStroke(
            sizeBorderListAttribute,
            colorBorderList
        )
    }

    fun setSizeBorderList(sizeBorderList: Int) {
        val colorBorderListAttribute =
            this.attributes?.getColor(
                R.styleable.CollapsibleList_colorBorderList,
                ContextCompat.getColor(context, DEFAULT_BORDER_COLOR_LIST)
            ) ?: ContextCompat.getColor(context, DEFAULT_BORDER_COLOR_LIST)

        (constraintLayoutParent?.background as? GradientDrawable)?.setStroke(
            sizeBorderList,
            colorBorderListAttribute
        )
    }

    fun setCornerBorderList(cornerRadius: Int) {
        (constraintLayoutParent?.background as? GradientDrawable)?.cornerRadius =
            cornerRadius.toFloat()
    }

    //endregion

}

abstract class CollapsibleListAdapter<T>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, RecyclerView.ViewHolder>(diffUtil)