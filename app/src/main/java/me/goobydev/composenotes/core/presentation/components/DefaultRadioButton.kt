package me.goobydev.composenotes.core.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/* Reusable radio button composable, used in sorting composable and font/theme select composables */
@Composable
fun DefaultRadioButton(
    text: String,
    selected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
  Row(
      modifier = modifier
          .clickable{
              onSelect()
                    },
      verticalAlignment = Alignment.CenterVertically
  ) {
      RadioButton(
          selected = selected,
          onClick = onSelect,
          colors = RadioButtonDefaults.colors(
              selectedColor = MaterialTheme.colors.secondary,
              unselectedColor = MaterialTheme.colors.secondary
          )
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(text = text, style = MaterialTheme.typography.body1)
  }
}
