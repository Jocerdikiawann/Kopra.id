package com.trian.module.ui.pages.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.trian.common.utils.network.DataStatus
import com.trian.common.utils.route.Routes
import com.trian.component.cards.CardHospital
import com.trian.component.cards.CardNotFound
import com.trian.data.viewmodel.TelemedicineViewModel
import kotlinx.coroutines.CoroutineScope

/**
 * Dashboard Reservation
 * Author PT Cexup Telemedicine
 * Created by Trian Damai
 * 11/09/2021
 */
@Composable
fun DashboardListHospital(
    modifier: Modifier =Modifier,
    scaffoldState: ScaffoldState= rememberScaffoldState(),
    scrollState: LazyListState,
    nav: NavHostController,
    scope: CoroutineScope,
    telemedicineViewModel: TelemedicineViewModel
){


    val hospitals by telemedicineViewModel.hospitalStatus.observeAsState()
    LaunchedEffect(key1 = scaffoldState) {
        telemedicineViewModel.getHospital {  }
    }

                LazyColumn(
                    state=scrollState,
                    verticalArrangement = Arrangement.spacedBy(3.dp),
                    contentPadding = PaddingValues( vertical = 8.dp),
                    content = {
                        when(hospitals){
                            is DataStatus.NoData -> {
                                item {
                                    CardNotFound()
                                }
                            }
                            is DataStatus.Loading -> {
                                //should show loading
                            }
                            is DataStatus.HasData -> {
                                items(count = hospitals?.data!!.size,itemContent = { index->
                                    CardHospital(
                                        hospital = hospitals?.data!![index]
                                    ) { _, _ ->
                                        nav.navigate(Routes.DETAIL_HOSPITAL) {
                                            launchSingleTop = true
                                        }
                                    }
                                })
                            }
                            null -> item {
                                CardNotFound()
                            }
                        }

                    })


}

@Preview
@Composable
fun PreviewDashboardReservation(){
    DashboardListHospital(
        scrollState = rememberLazyListState(),
        nav = rememberNavController() ,
        scope = rememberCoroutineScope(),
        telemedicineViewModel = viewModel()
    )
}