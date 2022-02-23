package com.Duong.crowdshipping.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.Duong.crowdshipping.R
import com.Duong.crowdshipping.model.Post
import com.Duong.crowdshipping.utils.LocationPermissionHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.gestures.MoveGestureDetector
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.geojson.Point
import com.mapbox.maps.*
import com.mapbox.maps.extension.style.expressions.dsl.generated.interpolate
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.gestures.OnMoveListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorBearingChangedListener
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigation
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.search.*
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import java.lang.ref.WeakReference
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.camera.view.MapboxRecenterButton
import com.mapbox.navigation.ui.maps.camera.view.MapboxRouteOverviewButton
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.utils.internal.toPoint
import android.R.attr.name
import android.content.DialogInterface
import android.net.Uri
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MapActivity : AppCompatActivity() {
    private lateinit var  targetLocation :Point
    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }
    private val mapboxReplayer = MapboxReplayer()
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
    private lateinit var mapboxNavigation: MapboxNavigation
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource
    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView
    private val navigationLocationProvider = NavigationLocationProvider()

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
        // not handled

        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            // update location puck's position on the map
            val currLat = BigDecimal(enhancedLocation.latitude).setScale(3,RoundingMode.HALF_EVEN)
            val currLng = BigDecimal(enhancedLocation.longitude).setScale(3,RoundingMode.HALF_EVEN)
            if(currLat == BigDecimal(targetFrom.latitude()).setScale(3,RoundingMode.HALF_EVEN)&&
                currLng == BigDecimal(targetFrom.longitude()).setScale(3,RoundingMode.HALF_EVEN)&&
                    btn.text.equals("Di chuyển đến chỗ nhận hàng")){
                btn.setText("Lấy hàng")
            }
            if(currLat == BigDecimal(targetTo.latitude()).setScale(3,RoundingMode.HALF_EVEN)&&
                currLng == BigDecimal(targetTo.longitude()).setScale(3,RoundingMode.HALF_EVEN)&&
                btn.text.equals("Di chuyển đến chỗ giao hàng")){
                btn.setText("Đã giao hàng")

            }
//            // update camera position to account for new location
//            Log.d("LocationnnnnCurr", currLat.toString()+"-"+currLng.toString())
//            Log.d("LocationnnnnTar", BigDecimal(targetFrom.latitude()).setScale(6,RoundingMode.HALF_EVEN).toString()+"-"+
//                    BigDecimal(targetFrom.longitude()).setScale(6,RoundingMode.HALF_EVEN).toString())
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            Log.d("Routessssss",locationMatcherResult.toString())
            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()


            // if this is the first location update the activity has received,
            // it's best to immediately move the camera to the current user location
            if (!firstLocationUpdateReceived) {

                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0) // instant transition
                        .build()
                )
            }

        }
    }
    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
// update the camera position to account for the progressed fragment of the route
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

// draw the upcoming maneuver arrow on the map
        val style = mapView.getMapboxMap().getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }
    }
    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            // generate route geometries asynchronously and render them
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapView.getMapboxMap().getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }
            // update the camera position to account for the new route
            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            viewportDataSource.evaluate()
        } else {
            // remove the route line and route arrow from the map
            val style =  mapView.getMapboxMap().getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
            }

            // remove the route reference from camera position evaluations
            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    private lateinit var locationPermissionHelper: LocationPermissionHelper


    private lateinit var mapView: MapView
    private lateinit var recenter:MapboxRecenterButton
    private lateinit var  routeOverview: MapboxRouteOverviewButton
    private lateinit var post: Post
    private lateinit var addressFrom: TextView
    private lateinit var fragile: TextView
    private lateinit var addressTo: TextView
    private lateinit var type: TextView
    private lateinit var message: ImageView
    private lateinit var call : ImageView
    private lateinit var btn:Button
    private lateinit var targetFrom:Point
    private lateinit var targetTo:Point
    private lateinit var  reference: DatabaseReference
    private lateinit var hashMap:HashMap<String,Any>

//    private lateinit var tripProgressCard: CardView
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map2)

        val intent = intent
        post = (intent.getSerializableExtra("Post") as Post?)!!
        targetFrom = intent.getSerializableExtra("TargetFrom") as Point
        targetTo = intent.getSerializableExtra("TargetTo") as Point

        mapView = findViewById(R.id.mapView)
        recenter = findViewById(R.id.recenter)
        routeOverview = findViewById(R.id.routeOverview)
        addressFrom = findViewById(R.id.addressFrom)
        addressTo = findViewById(R.id.addressTo)
        fragile = findViewById(R.id.fragile)
        type = findViewById(R.id.type)
        message = findViewById(R.id.btnMSG)
        call = findViewById(R.id.btnCall)
        btn = findViewById(R.id.button)
        locationPermissionHelper = LocationPermissionHelper(WeakReference(this))
        locationPermissionHelper.checkPermissions {
            onMapReady()
        }
        mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(this.applicationContext)
                    .accessToken(getString(R.string.mapbox_access_token))
            // comment out the location engine setting block to disable simulation
                    .locationEngine(replayLocationEngine)
                    .build()
            )
        }

        viewportDataSource = MapboxNavigationViewportDataSource(mapView.getMapboxMap())
        navigationCamera = NavigationCamera(
            mapView.getMapboxMap(),
            mapView.camera,
            viewportDataSource
        )

        mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            // shows/hide the recenter button depending on the camera state
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> {
                    recenter.visibility = View.INVISIBLE}
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> {
                    recenter.visibility = View.VISIBLE }
            }
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }
        val distanceFormatterOptions = mapboxNavigation.navigationOptions.distanceFormatterOptions
        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )
        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(this)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(this)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )
        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

        // initialize maneuver arrow view to draw arrows on the map
        val routeArrowOptions = RouteArrowOptions.Builder(this).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)


//        stop.setOnClickListener {
//            clearRouteAndStopNavigation()
//        }
        recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
            routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }

        // set initial sounds button state

        // start the trip session to being receiving location updates in free drive
        // and later when a route is set also receiving route progress updates
        mapboxNavigation.startTripSession()
        addressFrom.setText(addressFrom.text.toString()+" "+post.getAddressFrom()["Address"]+", đường "+ post.getAddressFrom()["Streets"] +", phường "+ post.getAddressFrom()["Wards"] +", quận "+post.getAddressFrom()["District"]+", "+post.getAddressFrom()["City"])
        addressTo.setText(addressTo.text.toString()+" "+post.getAddressTo()["Address"]+", đường "+post.getAddressTo().get("Streets")+", phường " +post.getAddressTo().get("Wards")+", quận "+post.getAddressTo().get("District")+", "+post.getAddressTo().get("City"))
        type.setText(type.text.toString()+post.description)
        if (!post.fragile||post.fragile==null){
            fragile.visibility = View.INVISIBLE
        }
        message.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MessageActivity::class.java)
            intent.putExtra("userid", post.createID)
            startActivity(intent)
        })
        call.setOnClickListener(View.OnClickListener {
            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_popup, null)
            val phoneFrom = view.findViewById<TextView>(R.id.phoneFrom)
            val phoneTo = view.findViewById<TextView>(R.id.phoneTo)
            val phoneFromWrap = view.findViewById<LinearLayout>(R.id.phoneFromWrap)
            val phoneToWrap = view.findViewById<LinearLayout>(R.id.phoneToWrap)
            phoneFrom.setText(phoneFrom.text.toString()+post.phoneFrom)
            phoneTo.setText(phoneTo.text.toString()+post.phoneTo)
            phoneFromWrap.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+post.phoneFrom)
                startActivity(intent)
            }
            phoneToWrap.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:"+post.phoneTo)
                startActivity(intent)
            }
            dialog.setContentView(view)
            dialog.show()
        })
        if(post.status.equals("1")||post.status.equals("0")){
            btn.setText("Di chuyển đến chỗ nhận hàng")
        }else{
            btn.setText("Di chuyển đến chỗ giao hàng")
        }
        btn.setOnClickListener {
            if(btn.text.equals("Lấy hàng")){
                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val currentDateandTime = sdf.format(Date())
                reference =
                    FirebaseDatabase.getInstance().getReference("Notification").child(post.createID)
                val pushedPostRef: DatabaseReference = reference.push()
                hashMap = HashMap<String, Any>()
                hashMap.put("MSG", "Người giao hàng cho đơn hàng "+ post.postID+" đã đến")
                hashMap.put("PostID", post.postID)
                hashMap.put("Time", currentDateandTime)
                pushedPostRef.key?.let { it1 -> hashMap.put("NotiID", it1) }
                hashMap.put("isseen", false)
                pushedPostRef.setValue(hashMap)
                btn.setText("Đã lấy hàng")
            }
            if(btn.text.equals("Đã lấy hàng")){
                val reference =
                    FirebaseDatabase.getInstance().getReference("Post").child(post.postID)
                val hashMap = java.util.HashMap<String, Any>()
                hashMap["Status"] = "2"
                reference.updateChildren(hashMap)
                btn.setText("Di chuyển đến chỗ giao hàng")
                clearRouteAndStopNavigation()
                findRoute(targetTo)
            }
            if(btn.text.equals("Đã giao hàng")){
                AlertDialog.Builder(this@MapActivity)
                    .setTitle("Thông báo")
                    .setMessage("Sau khi giao hàng thành công bạn cần đợi chủ đơn hàng xác nhận thành công để hoàn tất đơn hàng.") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setPositiveButton(
                        android.R.string.yes
                    ) { dialogInterface, i ->
                        val reference =
                            FirebaseDatabase.getInstance().getReference("Post").child(post.postID)
                        val hashMap = java.util.HashMap<String, Any>()
                        hashMap["Status"] = "3"
                        reference.updateChildren(hashMap).addOnCompleteListener {
                            clearRouteAndStopNavigation()
                            finish()
                        }

                    }.setNegativeButton(android.R.string.no, null)
                    .show()
            }

        }
    }

    private fun onMapReady() {

        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .zoom(10.0)
                .build()
        )
        mapView.getMapboxMap().loadStyleUri(
            Style.MAPBOX_STREETS
        ) {

            if(post.status.equals("1")||post.status.equals("0")){
                Log.d("Statusssssssssssss", post.status)
                findRoute(targetFrom)
            }else{
                Log.d("Statusssssssssssss", targetTo.toString())
                findRoute(targetTo)
            }
        }
        mapView.location.apply {
            setLocationProvider(navigationLocationProvider)

        // Uncomment this block of code if you want to see a circular puck with arrow.

        locationPuck = LocationPuck2D(
        bearingImage = ContextCompat.getDrawable(
        this@MapActivity,
        R.drawable.mapbox_navigation_puck_icon
        )
        )


        // When true, the blue circular puck is shown on the map. If set to false, user
        // location in the form of puck will not be shown on the map.
            enabled = true
        }
    }






    override fun onDestroy() {
        super.onDestroy()
        MapboxNavigationProvider.destroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        locationPermissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onStart() {
        super.onStart()
        mapboxNavigation.registerRoutesObserver(routesObserver)
        mapboxNavigation.registerRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.registerLocationObserver(locationObserver)
        mapboxNavigation.registerRouteProgressObserver(replayProgressObserver)

        if (mapboxNavigation.getRoutes().isEmpty()) {
            // if simulation is enabled (ReplayLocationEngine set to NavigationOptions)
            // but we're not simulating yet,
            // push a single location sample to establish origin
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point =Point.fromLngLat(105.800808, 21.039982)
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
        }


    }

    override fun onStop() {
        super.onStop()
        mapboxNavigation.unregisterRoutesObserver(routesObserver)
        mapboxNavigation.unregisterRouteProgressObserver(routeProgressObserver)
        mapboxNavigation.unregisterLocationObserver(locationObserver)
        mapboxNavigation.unregisterRouteProgressObserver(replayProgressObserver)
    }

    private fun findRoute(destination: Point) {
        Log.d("LastLocation", navigationLocationProvider.lastLocation.toString())
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return
        // execute a route request
        // it's recommended to use the
        // applyDefaultNavigationOptions and applyLanguageAndVoiceUnitOptions
        // that make sure the route request is optimized
        // to allow for support of all of the Navigation SDK features
        mapboxNavigation.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .coordinatesList(listOf(originPoint, destination))
                // provide the bearing for the origin of the request to ensure
                // that the returned route faces in the direction of the current user movement
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(mapboxNavigation.getZLevel(), null))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                // no impl
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {
                // no impl
                }
            }
        )
    }
    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
    // set routes, where the first route in the list is the primary route that
    // will be used for active guidance
        mapboxNavigation.setRoutes(routes)
    // start location simulation along the primary route
        startSimulation(routes.first())

    // show UI elements
        routeOverview.visibility = View.VISIBLE
//        tripProgressCard.visibility = View.VISIBLE

// move the camera to overview when new route is available
//        navigationCamera.requestNavigationCameraToOverview()
    }
    private fun clearRouteAndStopNavigation() {
        // clear
        mapboxNavigation.setRoutes(listOf())

        // stop simulation
        mapboxReplayer.stop()

        // hide UI elements
        routeOverview.visibility = View.INVISIBLE
    }
    private fun startSimulation(route: DirectionsRoute) {
        mapboxReplayer.run {
            stop()
            clearEvents()
            val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
            pushEvents(replayEvents)
            seekTo(replayEvents.first())
            play()
        }
    }
}


