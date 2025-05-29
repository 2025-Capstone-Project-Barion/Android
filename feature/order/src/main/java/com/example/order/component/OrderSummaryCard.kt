// ui/components/OrderSummaryCard.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.OrderSummary
import com.example.ui.theme.barrionColors
import com.example.ui.theme.Spacing
import com.example.ui.theme.CornerRadius
import java.text.NumberFormat
import java.util.*

@Composable
fun OrderSummaryCard(
    summary: OrderSummary,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.barrionColors.white
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(Spacing.Large)
        ) {
            Text(
                text = "정산 현황",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.barrionColors.grayBlack,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(Spacing.Medium))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                // 왼쪽: 완료/전체 건수
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.Large)
                ) {
                    Column {
                        Text(
                            text = "결제완료",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.barrionColors.grayMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${summary.completedCount}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.barrionColors.grayBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Column {
                        Text(
                            text = "합계",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.barrionColors.grayMedium
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${summary.totalCount}",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.barrionColors.grayBlack,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // 오른쪽: 총 금액
                Text(
                    text = "${formatter.format(summary.totalAmount)}원",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.barrionColors.primaryBlue,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}