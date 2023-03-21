package com.example.instagramfeedpreview.ui.component.instagram.adapter

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridDividerItemDecoration(private val dividerWidth: Int, private val dividerColor: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val spanCount = (parent.layoutManager as GridLayoutManager).spanCount

        // spanIndex는 현재 아이템이 속한 그리드 레이아웃의 열 인덱스입니다.
        val spanIndex = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex

        // 아이템이 첫번째 열이 아니라면 왼쪽에 구분선을 추가합니다.
        if (spanIndex > 0) {
            outRect.left = dividerWidth
        }
        // 아이템이 첫번째 행이 아니라면 위쪽에 구분선을 추가합니다.
        if (position >= spanCount) {
            outRect.top = dividerWidth
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(canvas, parent, state)
        val dividerPaint = Paint()
        dividerPaint.color = dividerColor

        // 모든 아이템에 대해 구분선을 그립니다.
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as GridLayoutManager.LayoutParams
            val spanIndex = params.spanIndex

            // 아이템이 첫번째 열이 아니라면 왼쪽에 구분선을 그립니다.
            if (spanIndex > 0) {
                val dividerLeft = child.left - dividerWidth
                val dividerTop = child.top
                val dividerRight = child.left
                val dividerBottom = child.bottom + dividerWidth
                canvas.drawRect(dividerLeft.toFloat(), dividerTop.toFloat(), dividerRight.toFloat(), dividerBottom.toFloat(), dividerPaint)
            }

            // 아이템이 첫번째 행이 아니라면 위쪽에 구분선을 그립니다.
            if (params.viewAdapterPosition >= parent.layoutManager!!.childCount) {
                val dividerLeft = child.left
                val dividerTop = child.top - dividerWidth
                val dividerRight = child.right
                val dividerBottom = child.top
                canvas.drawRect(dividerLeft.toFloat(), dividerTop.toFloat(), dividerRight.toFloat(), dividerBottom.toFloat(), dividerPaint)
            }
        }
    }
}
