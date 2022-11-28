package com.programmersbox.jakepurple13libraries.screens

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.programmersbox.diamondloader.*
import com.programmersbox.jakepurple13libraries.ScaffoldTop
import com.programmersbox.jakepurple13libraries.nextColor
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Preview(device = Devices.NEXUS_5X, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiamondLoaderScreen() {
    ScaffoldTop(screen = Screen.DiamondLoader) { p ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(p)
        ) {
            var diamond by remember { mutableStateOf(Random.nextFloat()) }

            var primaryColor by remember { mutableStateOf(Random.nextColor(a = 255)) }
            var backgroundColor by remember { mutableStateOf(Random.nextColor(a = 255)) }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = { diamond = Random.nextFloat() }) { Text("Random Progress") }
                Button(
                    onClick = {
                        primaryColor = Random.nextColor(a = 255)
                        backgroundColor = Random.nextColor(a = 255)
                    }
                ) { Text("Random Colors") }
            }

            Slider(value = diamond, onValueChange = { diamond = it })
            Text("${diamond * 100f}%")

            val primaryColorAnimation by animateColorAsState(targetValue = primaryColor)
            val backgroundColorAnimation by animateColorAsState(targetValue = backgroundColor)
            val diamondProgress by animateFloatAsState(
                targetValue = diamond,
                animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                DiamondLoader(
                    progress = diamondProgress,
                    progressColor = primaryColorAnimation,
                    emptyColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )

                CircularProgressIndicator(
                    progress = diamondProgress,
                    color = primaryColorAnimation,
                    modifier = Modifier.size(100.dp)
                )

                CircularProgressIndicator(
                    color = primaryColorAnimation,
                    modifier = Modifier.size(100.dp)
                )

            }

            LinearProgressIndicator(
                color = primaryColorAnimation,
                trackColor = backgroundColorAnimation,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = diamondProgress,
                color = primaryColorAnimation,
                trackColor = backgroundColorAnimation,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                OuterDiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
                InnerDiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Slider(value = diamond, onValueChange = { diamond = it })
            Text("${diamond * 100f}%")

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
                DiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
                DiamondLoader(
                    progress = diamondProgress,
                    progressColor = primaryColorAnimation,
                    emptyColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
            }

            Slider(value = diamond, onValueChange = { diamond = it })
            Text("${diamond * 100f}%")

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                OuterDiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
                OuterDiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                InnerDiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
                InnerDiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    image = ImageBitmap.imageResource(id = android.R.drawable.ic_menu_add)
                )
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                ReverseDiamondProgressIndicator(
                    progress = diamondProgress,
                    reverse = true,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    modifier = Modifier.size(100.dp)
                )
                ReverseDiamondProgressIndicator(
                    progress = diamondProgress,
                    reverse = false,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    modifier = Modifier.size(100.dp)
                )
            }

            Slider(value = diamond, onValueChange = { diamond = it })
            Text("${diamond * 100f}%")

            var sizeOfLoaders by remember { mutableStateOf(100f) }
            Text("Size (Default 100): ${sizeOfLoaders.toInt()}")
            Slider(
                value = sizeOfLoaders,
                onValueChange = { sizeOfLoaders = it },
                valueRange = 25f..200f
            )

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(animateDpAsState(targetValue = sizeOfLoaders.dp).value)
                )
                DiamondProgressIndicator(
                    progress = diamondProgress,
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(animateDpAsState(targetValue = sizeOfLoaders.dp).value)
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(200.dp),
                    animationSpec = keyframes {
                        durationMillis = (1332 * 0.5).toInt() * 2 * 2
                        0f at (1332 * 0.5).toInt() * 2 with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                        200f at durationMillis
                    }
                )
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(200.dp),
                    animationSpec = keyframes {
                        durationMillis = (1332 * 0.5).toInt() * 2
                        0f at (1332 * 0.5).toInt() * 2 with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                        200f at durationMillis
                    }
                )
            }

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    animationSpec = keyframes {
                        durationMillis = (1332 * 0.5).toInt() * 2 * 2
                        0f at (1332 * 0.5).toInt() with CubicBezierEasing(0.4f, 0f, 0.2f, 1f)
                        200f at durationMillis
                    }
                )

                DiamondProgressIndicator(
                    innerColor = primaryColorAnimation,
                    outerColor = backgroundColorAnimation,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(100.dp),
                    animationSpec = tween(1500)
                )
            }
        }
    }
}