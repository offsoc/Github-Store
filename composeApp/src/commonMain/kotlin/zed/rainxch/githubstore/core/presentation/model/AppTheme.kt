package zed.rainxch.githubstore.core.presentation.model

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import githubstore.composeapp.generated.resources.Res
import githubstore.composeapp.generated.resources.logout
import githubstore.composeapp.generated.resources.theme_amber
import githubstore.composeapp.generated.resources.theme_dynamic
import githubstore.composeapp.generated.resources.theme_forest
import githubstore.composeapp.generated.resources.theme_ocean
import githubstore.composeapp.generated.resources.theme_purple
import githubstore.composeapp.generated.resources.theme_slate
import org.jetbrains.compose.resources.stringResource
import zed.rainxch.githubstore.core.presentation.theme.amberOrangeDark
import zed.rainxch.githubstore.core.presentation.theme.amberOrangeLight
import zed.rainxch.githubstore.core.presentation.theme.deepPurpleDark
import zed.rainxch.githubstore.core.presentation.theme.deepPurpleLight
import zed.rainxch.githubstore.core.presentation.theme.forestGreenDark
import zed.rainxch.githubstore.core.presentation.theme.forestGreenLight
import zed.rainxch.githubstore.core.presentation.theme.oceanBlueDark
import zed.rainxch.githubstore.core.presentation.theme.oceanBlueLight
import zed.rainxch.githubstore.core.presentation.theme.slateGrayDark
import zed.rainxch.githubstore.core.presentation.theme.slateGrayLight

enum class AppTheme(
    val lightScheme: ColorScheme?,
    val darkScheme: ColorScheme?,
    val primaryColor: Color?
) {
    DYNAMIC(null, null, null),
    OCEAN(oceanBlueLight, oceanBlueDark, Color(0xFF2A638A)),
    PURPLE(deepPurpleLight, deepPurpleDark, Color(0xFF6750A4)),
    FOREST(forestGreenLight, forestGreenDark, Color(0xFF356859)),
    SLATE(slateGrayLight, slateGrayDark, Color(0xFF535E6C)),
    AMBER(amberOrangeLight, amberOrangeDark, Color(0xFF8B5000));

    @Composable
    fun displayName(): String = stringResource(
        when (this) {
            DYNAMIC -> Res.string.theme_dynamic
            OCEAN -> Res.string.theme_ocean
            PURPLE -> Res.string.theme_purple
            FOREST -> Res.string.theme_forest
            SLATE -> Res.string.theme_slate
            AMBER -> Res.string.theme_amber
        }
    )

    companion object {
        fun fromName(name: String?): AppTheme =
            entries.find { it.name == name } ?: OCEAN
    }
}
