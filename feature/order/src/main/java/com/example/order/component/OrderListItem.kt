import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domain.model.Order
import com.example.domain.model.OrderStatus
import com.example.ui.theme.barrionColors
import java.text.NumberFormat
import java.util.*

@Composable
fun OrderListItem(
    order: Order,
    onDeleteClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = NumberFormat.getNumberInstance(Locale.KOREA)

    // 환불 여부 판단 (음수 금액 = 환불)
    val isRefunded = order.totalAmount < 0

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.barrionColors.white
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 주문번호와 상태칩
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "주문번호 ${order.orderId}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.barrionColors.grayBlack
                )

                OrderStatusChip(status = order.status)
            }

            // 주문 시간
            Text(
                text = "주문시각: ${order.orderTime}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.barrionColors.grayMedium
            )

            // 주문 금액
            Text(
                text = "주문금액: ${formatter.format(order.totalAmount)}원",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isRefunded) {
                    MaterialTheme.barrionColors.error
                } else {
                    MaterialTheme.barrionColors.primaryBlue
                }
            )

            // 취소 버튼 (환불된 주문에는 표시하지 않음)
            if (!isRefunded) {
                Button(
                    onClick = { onDeleteClick(order.orderId) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.barrionColors.error
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "주문 취소",
                        color = MaterialTheme.barrionColors.white,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun OrderListItemPreview() {
    // 일반 주문 예시
    val sampleOrder = Order(
        orderId = 1001,
        storeId = 1,
        orderTime = "2025.05.09 10:25",
        totalAmount = 12500,
        status = OrderStatus.RECEIVED
    )

    OrderListItem(
        order = sampleOrder,
        onDeleteClick = { }
    )
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun OrderListItemRefundPreview() {
    // 환불 주문 예시
    val refundOrder = Order(
        orderId = 1002,
        storeId = 1,
        orderTime = "2025.05.09 09:15",
        totalAmount = -8500,
        status = OrderStatus.CANCELLED
    )

    OrderListItem(
        order = refundOrder,
        onDeleteClick = { }
    )
}