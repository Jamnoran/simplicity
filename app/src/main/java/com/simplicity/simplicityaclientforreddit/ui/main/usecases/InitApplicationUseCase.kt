package com.simplicity.simplicityaclientforreddit.ui.main.usecases

import com.simplicity.simplicityaclientforreddit.ui.main.io.room.RoomDB
import com.simplicity.simplicityaclientforreddit.ui.main.io.settings.SettingsSP

class InitApplicationUseCase {
    fun init(firstTime: Boolean){
        if(firstTime){
            val db = RoomDB()
            val defaultHiddenSubs = "Superstonk,Minecraft,Genshin_Impact,ich_iel,pokemon,IndianDankMemes,PewdiepieSubmissions,tf2,halo,formula1,Genshin_Impact_Leaks,GlobalOffensive,ik_ihe,Hololive,HollowKnightMemes,dankinindia,traps,pokemongo,SaimanSays,kpop,2007scape,hungary,u_BabiMunizTS,Turkey,VALORANT,Warframe,EscapefromTarkov,FemBoys,HairyPussy,trans,france,twinks,GoneWildTrans,straightturnedgay,transporn,Sissies,Terraria,nba,amcstock,CFB,CollegeBasketball,sports,hockey,de,bigdickgirl,Tgirls,Colts,baseball,guns,thenetherlands,DadsGoneWild,trapsarentgay,boypussy,OnlyIfShesPackin,CryptoCurrency,HydroHomies,tscum,IsThisaGirl,WritingPrompts,traphentai,gay_irl,buffalobills,sissycaptions,ClashOfClans,bigclit,crossdressing,HungTwinks,MassiveCock,TransGoneWild,Gunners,MTFSelfieTrain,gayporn,formuladank,ksi,OnePiece,GOONED,AlphaMalePorn,Shemales,gayfurryporn,foreskin,Hairy,gaybros,portugal,LiverpoolFC,chubby,FurryPornSubreddit,broslikeus,rance"
            for(sub in defaultHiddenSubs.split(",")){
                db.hideSub(sub)
            }
        }
    }
}