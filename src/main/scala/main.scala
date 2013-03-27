package scalacrawler

import akka.actor._
import akka.dispatch.Future
import spray.can.client.HttpClient
import spray.client.HttpConduit
import spray.io._
import spray.util._
import spray.http._
import HttpMethods._


object Main {

  def main(args: Array[String]) {
    implicit val system = ActorSystem()
    val touples = urls.map(split _).toList


    val t1 = System.currentTimeMillis()

    try{
      val futures = touples.map(x => {print(x);doRequest(x._1, x._2, system).await})
      //    val daFuture = Future.sequence(futures)
      //    daFuture.await
      print(System.currentTimeMillis() - t1)
      //    print(daFuture.await)
    } finally {
      system.shutdown()
    }
  }


  def split(urlS: String) = {
    val url = new java.net.URL(urlS)
    (url.getHost, url.getPath)
  }

  def doRequest(host: String, path: String, system: ActorSystem): Future[HttpResponse] = {
    try {
      val ioBridge = IOExtension(system).ioBridge()
      val cli = system.actorOf(Props(new HttpClient(ioBridge)))
      val conduit = system.actorOf(
        props = Props(new HttpConduit(cli, host, 80))
      )

      val pipeline = HttpConduit.sendReceive(conduit)

      pipeline(HttpRequest(method = GET, uri = "path"))
    }
  }




  def urls = Array("""http://beta.tech-specs.com/""")// 'http://www.epinions.com/reviews/Agfa_ePhoto_CL50_Digital_Camera' 'http://www.rundrivers.com/category-cl/cl-s801-4ch-download-driver.html' 'http://www.nodevice.com.pt/driver/ePhoto_1280/get35613.html' 'http://driveraccess.com/app/pages/agfa/cl-18,-ephoto-cl18,-ephoto-cl18-driver.html' 'http://www.nodevice.es/driver/ePhoto_1280/get35613.html' 'http://www.imaging-resource.com/NEWSARCH/arc1-1999.html' 'http://www.driversdown.com/driverslist/s_102_1.shtml' 'http://www.oldtimercameras.com/search/subequipsearch.asp?EquipSubType_ID=2' 'http://www.webcam-driver.com/logitech/free-download-agfa-ephoto-cl-50-camera-driver-pw-18.html' 'http://www.dpreview.com/products/agfa/compacts/agfa_cl30_clik' 'http://www.nodevice.com/driver/company/AGFA/Digital_Cameras.html' 'http://www.agfa.com/digicam_scanner_drivers/' 'http://www.agfa.com/' 'http://www.nodevice.com/driver/ePhoto_780c/get35611.html' 'http://www.ojolink.net/download/free/category.asp?cat=12&subcat=157&subname=Cameras%20Drivers&name=Drivers&pages=6' 'http://list.driverguide.com/list/company50/page2/index.html' 'http://www.dansdata.com/cl30clik.htm' 'http://www.softitem.com/driver-delcop-cl-3005-w-driver' 'http://www.driverscup.com/agfa-drivers/agfa-snapscan-e52-win7-driver.html' 'http://www.dpreview.com/news/1999/08' 'http://www.ebay.com/sch/i.html?_nkw=Agfa+Digital+Cameras' 'http://www.ojolink.com/Drivers-de-camaras-Agfa-ePhoto-1280/' 'http://www.webcam-driver.com/page/2' 'http://drivers.softpedia.com/get/SCANNER-Digital-CAMERA-WEBCAM/AGFA/' 'http://www.driverskit.com/Camera/Agfa.html' 'http://www.driver100.com/developer/AGFA.html' 'http://www.camera-drivers.com/companies/50.htm' 'http://static.agfa.com/digicam_scanner_drivers/pdf/ephoto_cl50_ug_2002-07-04_en.pdf' 'http://www.simaproducts.com/support/files/SUP-70_User_manual.pdf' 'http://drivers.softpedia.com/get/SCANNER-Digital-CAMERA-WEBCAM/AGFA/Agfa-ePhoto-CL-50-serial-driver.shtml' 'http://drivers.downseeker.com/download/45829/agfa-ephoto-307-780-780c-1280-1680-serial-pw-171/' 'http://www.nodevice.com.pt/driver/company/AGFA/Digital_Cameras.html' 'http://www.fixya.com/support/p157220-agfa_ephoto_cl18_digital_camera' 'http://www.topsofts.com/driver/index.html' 'http://www.soft32download.com/developer/AGFA.html' 'http://www.ephotozine.com/equipment/manuals/Agfa/1' 'http://italian.softpicks.net/drivers/Scanner_fotocamera_digitale_webcam-7/Agfa-24/1.htm' 'http://www.oldtimercameras.com/butkus/search/mfrsearch.asp?MainMfr=1' 'http://www.steves-digicams.com/cl18.html' 'http://www.dpreview.com/news/0004/00041201agfacl18.asp' 'http://www.steves-digicams.com/cl30.html' 'http://list.driverguide.com/list/company50/device2/index.html' 'http://www.retrevo.com/s/AgfaPhoto-CL-50-Digital-Cameras-review-manual/id/23014dj498/t/1-2/' 'http://www.nodevice.com/driver/ePhoto_780/get35610.html' 'http://www.nodevice.com/driver/company/AGFA.html' 'http://drivers.softpedia.com/get/SCANNER-Digital-CAMERA-WEBCAM/AGFA/Agfa-ePhoto-CL-18-USB-Driver.shtml' 'http://www.helpdrivers.com/cameras/Agfa/' 'http://www.batterybank.net/digital/master/agfa.html'""".replace("\'","").split(' ')
}
