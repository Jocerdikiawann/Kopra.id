package com.trian.component.cards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trian.component.R
import com.trian.component.ui.theme.*
import com.trian.domain.models.Article

@Composable
fun CardArticle(title: String,
                subtitle:String,
                caption:String,
                onClick:(article:Article,index:Int)->Unit
){
    Card (
        shape = RoundedCornerShape(topEndPercent = 30),
    ){
        Box(modifier = Modifier
            .background(
                color = Color.Black
            )
            .paint(
                painter = painterResource(
                    id = R.drawable.doctor_dummy
                )
            )
        )
        Column(modifier = Modifier.padding(all = 10.dp)) {
            Text(text = title,color= Color.White,fontSize = 20.sp)
            Text(text= subtitle,color= Color.White,fontSize = 20.sp)
            Text(text = caption,color= Color.White,fontWeight = FontWeight.Light,fontSize = 10.sp)
        }
    }
}

@Composable
fun CardArticleFullWidth(
    modifier: Modifier=Modifier,
    title: String,
    subtitle:String,
    caption:String,
    subcaption:String,
    onClick:(article:Article,index:Int)->Unit
){
   Card(
       shape = RoundedCornerShape(
           topEnd = 10.dp,
           topStart = 10.dp
       ),
   ) {
       Column() {
           Image(
               painter = painterResource(id = R.drawable.doctor_dummy),
               contentDescription = "image",
               contentScale = ContentScale.Crop,
               modifier = Modifier.fillMaxWidth()
           )
           Row(
               modifier = modifier
                   .padding(12.dp)
                   .fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
           ){
               Text(
                   text = title,
                   fontSize = 11.sp,
                   fontWeight = FontWeight.Normal,
                   color = Color(0xFF6E798C))
               Text(
                   text = subtitle,
                   fontSize = 11.sp,
                   fontWeight = FontWeight.Normal,
                   color = Color(0xFF6E798C)
               )
           }
           Text(
               text = caption,
               fontSize = 20.sp,
               fontWeight = FontWeight.Medium,
               color = Color(0xFF081F32),
               modifier = modifier.padding(horizontal = 12.dp)
           )
           Spacer(modifier = modifier.height(5.dp))
           Text(
               text = subcaption,
               fontSize = 13.sp,
               fontWeight = FontWeight.Normal,
               maxLines = 3,
               color = Color(0xFF374A59),
               modifier = modifier.padding(horizontal = 12.dp) ,
               overflow = TextOverflow.Ellipsis,
           )
           Spacer(modifier = modifier.height(5.dp))
           Row(modifier = modifier
               .fillMaxWidth()
               .padding(12.dp),
               verticalAlignment = Alignment.CenterVertically,
               horizontalArrangement = Arrangement.SpaceBetween,
           ) {
              Row(verticalAlignment = Alignment.CenterVertically){
                  Image(
                      painter = painterResource(id = R.drawable.doctor_dummy),
                      contentDescription = "icon",
                      modifier = modifier
                          .size(40.dp)
                          .clip(CircleShape)
                          .fillMaxWidth(),
                      contentScale = ContentScale.Crop,
                  )
                  Spacer(modifier = modifier.width(10.dp))
                  Text(text = stringResource(R.string.datadummyarticlename),style = MaterialTheme.typography.subtitle1)
              }
               Row(verticalAlignment = Alignment.CenterVertically){
                   Text(text = stringResource(R.string.datadummyarticlereadmore),style = MaterialTheme.typography.subtitle1)
                   Icon(Icons.Filled.ArrowRight,"")
               }
           }
       }
   }
}

@Composable
fun CardArticleDetail(m:Modifier=Modifier,article:Article,onClick:(article:Article,index:Int)->Unit){
    Card(shape = RoundedCornerShape(10.dp)){
        Row(){
            Image(painter = painterResource(id = R.drawable.doctor_dummy,),modifier = m.height(140.dp).width(120.dp).fillMaxWidth(), contentDescription = "",contentScale= ContentScale.Crop)
            Column(modifier = m.padding(5.dp)) {
                Row (horizontalArrangement = Arrangement.SpaceBetween, modifier = m.fillMaxWidth()){
                    Text(text = article.category,fontSize = 10.sp)
                    Text(text = "20 Days Ago",fontSize = 10.sp)
                }
                Spacer(modifier = m.height(10.dp))
                Text(text = article.title,fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF081F32),)
                Spacer(modifier = m.height(10.dp))
                Text(text = article.content,fontSize = 10.sp,maxLines = 3,overflow = TextOverflow.Ellipsis)

            }
        }
    }
}

@Preview
@Composable
fun PreviewCardArticle(){
    CardArticle(title="3-Month",subtitle = "Pilates Beginner",caption = "By Sarah Allen",onClick = {article, index ->  })
}

@Preview
@Composable
fun PreviewCardArticleFullWidth(){
    CardArticleFullWidth(
        title=stringResource(R.string.datadummyarticletitle),
        subtitle = stringResource(R.string.datadummyarticledate),
        caption = stringResource(R.string.datadummyarticlecaption),
        subcaption =
        stringResource(R.string.datadummyarticlesubcaption),onClick = { article, index ->  })
}

@Preview
@Composable
fun PreviewCardArticleDetail(){
    CardArticleDetail(
        onClick = {article, index ->  },
        article = Article(
            category = "TIPS KESEHATAN",
            categorySlug = "tips-kesehatan",
            content = stringResource(R.string.datadummyarticlesubcaption),
            id = 1,
            slug = "5-cara-menjaga-kesehatan-tubuh",
            thumb = "",
            thumbOriginal = "",
            title =stringResource(R.string.datadummyarticlecaption),
        )
    )
}