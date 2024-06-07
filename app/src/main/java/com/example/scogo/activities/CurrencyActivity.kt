package com.example.scogo.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.scogo.R
import com.example.scogo.responseModel.CoinResponse
import com.example.scogo.ui.theme.ScogoTheme
import com.example.scogo.viewModels.CoinViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class CurrencyActivity : ComponentActivity() {
    private val myViewModel by viewModels<CoinViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScogoTheme {
                CoinListScreen(viewModel = myViewModel)
            }
        }
    }

    //->> Main UI body
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun CoinListScreen(viewModel: CoinViewModel) {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(Color.White, darkIcons = true)

        val coins by viewModel.coins.observeAsState(emptyList())
        val (selectedCoin, setSelectedCoin) = remember { mutableStateOf<CoinResponse?>(null) }
        var isRefreshing by remember { mutableStateOf(false) }
        val isLoading by viewModel.isLoading.observeAsState(false)

      //  Log.e("dataIs: ", "$coins")

        var text by remember { mutableStateOf("") }
        val filteredCoins = if (text.isEmpty()) coins else coins.filter {
            it.name.contains(text, ignoreCase = true) || it.symbol.contains(text, ignoreCase = true)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Search...") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Black
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
            )

            SwipeRefresh(
                state = SwipeRefreshState(isRefreshing),
                onRefresh = {
                    isRefreshing = true
                    viewModel.fetchCoins()
                    isRefreshing = false
                }
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {
                        items(filteredCoins) { coin ->
                            CoinItem(coin, onClick = { setSelectedCoin(coin) })
                        }
                    }
                }
            }

            selectedCoin?.let {
                CoinDetailDialog(coin = it, onDismiss = { setSelectedCoin(null) })
            }
        }
    }

    //->> Coin item
    @Composable
    private fun CoinItem(coin: CoinResponse, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))
                .clickable(onClick = onClick)
        ) {
            Image(
                painterResource(id = R.drawable.dummy_card),
                contentDescription = "dummyCard",
                Modifier.fillMaxWidth(), contentScale = ContentScale.Crop
            )
            Row(modifier = Modifier.padding(start = 20.dp, top = 5.dp)) {
                Text(
                    text = "${coin.rank}. ${coin.name}",
                    color = colorResource(id = R.color.colorWhite),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(2.dp))
                Text(
                    modifier = Modifier.align(Alignment.Bottom),
                    text = "(${coin.symbol})",
                    color = colorResource(id = R.color.colorWhite),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    //->> Showing dialog for coin details
    @Composable
    private fun CoinDetailDialog(coin: CoinResponse, onDismiss: () -> Unit) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "Coin Details")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Name: ${coin.name}")
                    Text(text = "Symbol: ${coin.symbol}")
                    Text(text = "Rank: ${coin.rank}")
                    Text(text = "Active: ${coin.is_active}")
                    Text(text = "New: ${coin.is_new}")
                }
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text("Close")
                }
            }
        )
    }
}
