// ui/components/OrderStatusChip.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.OrderStatus
import com.example.ui.theme.barrionColors

@Composable
fun OrderStatusChip(
    status: OrderStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor) = when (status.colorType) {
        "blue" -> MaterialTheme.barrionColors.primaryBlue to MaterialTheme.barrionColors.white
        "gray" -> MaterialTheme.barrionColors.grayMedium to MaterialTheme.barrionColors.white
        "red" -> MaterialTheme.barrionColors.error to MaterialTheme.barrionColors.white
        else -> MaterialTheme.barrionColors.primaryBlue to MaterialTheme.barrionColors.white
    }

    Text(
        text = status.displayName,
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .padding(horizontal = 10.dp, vertical = 5.dp),
        color = textColor,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium
    )
}
