// ui/screen/OrderScreen.kt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.order.type.OrderEffect
import com.example.order.type.OrderIntent
import com.example.order.viewmodel.OrderViewModel
import com.example.ui.theme.barrionColors
import com.example.ui.theme.Spacing
import com.example.ui.theme.CornerRadius
import com.example.ui.components.buttons.BarrionNavigationButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    var orderToDelete by rememberSaveable { mutableIntStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Effect 처리
    LaunchedEffect(viewModel.effect) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OrderEffect.ShowError -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
                is OrderEffect.ShowDeleteSuccess -> {
                    snackbarHostState.showSnackbar("${effect.orderNumber}번 주문이 삭제되었습니다")
                }
                is OrderEffect.NavigateToOrderDetail -> {
                    // 주문 상세 화면으로 네비게이션
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "주문 관리",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.handleIntent(OrderIntent.RefreshData) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "새로고침"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.barrionColors.white,
                    titleContentColor = MaterialTheme.barrionColors.grayBlack,
                    actionIconContentColor = MaterialTheme.barrionColors.grayBlack
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.barrionColors.white
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(Spacing.Medium)
        ) {
            // 정산 현황 카드
            if (state.summary != null) {
                OrderSummaryCard(
                    summary = state.summary!!,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(Spacing.Large))
            }

            // 주문 내역 제목 - 왼쪽 배치로 변경
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 왼쪽: 주문 내역 제목
                Text(
                    text = "주문 내역",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.barrionColors.grayBlack,
                    fontWeight = FontWeight.Bold
                )

                // 오른쪽: 총 건수
                Text(
                    text = "총 ${state.orders.size}건",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.barrionColors.grayMedium
                )
            }

            Spacer(modifier = Modifier.height(Spacing.Medium))

            // 주문 목록
            when {
                state.isLoading -> {
                    OrderLoadingState()
                }

                state.isEmpty -> {
                    OrderEmptyState()
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.orders) { order ->
                            OrderListItem(
                                order = order,
                                onDeleteClick = { orderId ->
                                    orderToDelete = orderId
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // 삭제 확인 다이얼로그
    if (showDeleteDialog) {
        OrderDeleteDialog(
            orderNumber = orderToDelete.toString(),
            onConfirm = {
                viewModel.handleIntent(OrderIntent.DeleteOrder(orderToDelete))
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }
}

@Composable
private fun OrderLoadingState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.XXLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            color = MaterialTheme.barrionColors.primaryBlue
        )

        Spacer(modifier = Modifier.height(Spacing.Medium))

        Text(
            text = "주문 내역을 불러오는 중...",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.barrionColors.grayMedium
        )
    }
}

@Composable
private fun OrderEmptyState(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.XXLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "주문 내역이 없습니다",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.barrionColors.grayMedium
        )

        Spacer(modifier = Modifier.height(Spacing.XSmall))

        Text(
            text = "새로운 주문이 들어오면 여기에 표시됩니다",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.barrionColors.grayMedium
        )
    }
}

@Composable
private fun OrderDeleteDialog(
    orderNumber: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        containerColor = MaterialTheme.barrionColors.white,
        shape = RoundedCornerShape(CornerRadius.Large),
        title = {
            Text(
                text = "${orderNumber}번을 취소처리 하시겠습니까?",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.barrionColors.grayBlack,
                modifier = Modifier.padding(bottom = Spacing.Medium)
            )
        },
        confirmButton = {
            BarrionNavigationButtons(
                leftText = "아니오",
                rightText = "예",
                onLeftClick = onDismiss,
                onRightClick = onConfirm,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}