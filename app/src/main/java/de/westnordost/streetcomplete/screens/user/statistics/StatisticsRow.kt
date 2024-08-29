package de.westnordost.streetcomplete.screens.user.statistics

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.westnordost.streetcomplete.R
import de.westnordost.streetcomplete.ui.ktx.pxToDp

@Composable
fun StatisticsRow(
    title: @Composable () -> Unit,
    count: Int,
    maxCount: Int,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.primary,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        title()
        BoxWithConstraints(Modifier.weight(1f)) {
            val textMeasurer = rememberTextMeasurer(1)
            val textStyle = MaterialTheme.typography.body1
            val textSize = textMeasurer.measure(count.toString(), textStyle).size
            val availableBarWidth = maxWidth - textSize.width.pxToDp() - 8.dp
            val barWidth = (availableBarWidth * count / maxCount)
            val barHeight = textSize.height.pxToDp() + 4.dp

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier
                    .width(barWidth)
                    .height(barHeight)
                    .background(color, RoundedCornerShape(
                        topStart = 2.dp,
                        bottomStart = 2.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp
                    ))
                )
                Text(
                    text = count.toString(),
                    style = textStyle,
                    maxLines = 1
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewStatisticsRow() {
    StatisticsRow(
        title = {
            Image(
                painter = painterResource(R.drawable.ic_building_allotment_house),
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
        },
        count = 10000,
        maxCount = 10000
    )
}
