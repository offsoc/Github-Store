package zed.rainxch.githubstore.feature.search.presentation.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.dp
import zed.rainxch.githubstore.feature.search.domain.model.SortBy

@Composable
fun SortByBottomSheet(
    sortByOptions: List<SortBy>,
    selectedSortBy: SortBy,
    onSortBySelected: (SortBy) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = "Close")
            }
        },
        title = {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleMedium
            )
        },
        text = {
            Column(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                sortByOptions.forEach { option ->
                    val isSelected = option == selectedSortBy
                    TextButton(
                        onClick = {
                            onSortBySelected(option)
                            onDismissRequest()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = option.displayText() + if (isSelected) "  ✓" else "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    )
}