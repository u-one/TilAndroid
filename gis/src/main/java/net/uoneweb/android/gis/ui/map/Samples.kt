package net.uoneweb.android.gis.ui.map

import org.maplibre.geojson.Feature

object Samples {


    val featurePointJson = """
    Feature{
        type=Feature,
        bbox=null,
        id=36481525030,
        geometry=Point{
            type=Point, bbox=null, coordinates=[133.77040296792984, 34.5966043503127]
        },
        properties={
            "subclass":"restaurant","name_int":"hiroshi sakae doumoto ten","rank":311,"name_de":"廣榮堂本店",
            "name_en":"廣榮堂本店",
            "name:nonlatin":"廣榮堂本店",
            "name:latin":"hiroshi sakae doumoto ten",
            "name":"廣榮堂本店",
            "class":"restaurant"
        }
    }       
    """.trimIndent()

    val featurePoint = Feature.fromJson(featurePointJson)

    val featureLineStringJson = """
    Feature{
        type=Feature, bbox=null, id=191332768,
        geometry=LineString{
            type=LineString,
            bbox=null,
            coordinates=[
                Point{type=Point, bbox=null, coordinates=[133.7709340453148, 34.596482914951196]},
                Point{type=Point, bbox=null, coordinates=[133.77085089683533, 34.596401221971746]}
            ]
        },
        properties={"bicycle":"yes","class":"minor","horse":"yes","access":"no","surface":"paved","layer":1,"foot":"yes","brunnel":"bridge"}
    }    
    """.trimIndent()

    val featureLineString = Feature.fromJson(featureLineStringJson)

    val featurePolygonJson = """
    Feature{
        type=Feature, bbox=null, id=1186639546,
        geometry=Polygon{
            type=Polygon, 
            bbox=null,
            coordinates=[
                [Point{type=Point, bbox=null, coordinates=[133.77051293849945, 34.596763319972155]}, Point{type=Point, bbox=null, coordinates=[133.77047270536423, 34.596842804687796]}, Point{type=Point, bbox=null, coordinates=[133.77050757408142, 34.596856052133035]}, Point{type=Point, bbox=null, coordinates=[133.77057194709778, 34.59680747815692]}, Point{type=Point, bbox=null, coordinates=[133.77066850662231, 34.59689358654967]}, Point{type=Point, bbox=null, coordinates=[133.77069532871246, 34.59688033911041]}, Point{type=Point, bbox=null, coordinates=[133.77071410417557, 34.596856052133035]}, Point{type=Point, bbox=null, coordinates=[133.77079457044601, 34.596778775339516]}, Point{type=Point, bbox=null, coordinates=[133.77071410417557, 34.596701498474076]}, Point{type=Point, bbox=null, coordinates=[133.77081602811813, 34.59662642945027]}, Point{type=Point, bbox=null, coordinates=[133.770791888237, 34.5966043503127]}, Point{type=Point, bbox=null, coordinates=[133.77081334590912, 34.596586686998435]}, Point{type=Point, bbox=null, coordinates=[133.77079457044601, 34.5965690236804]}, Point{type=Point, bbox=null, coordinates=[133.77051293849945, 34.596763319972155]}]
            ]
        },
        properties={"class":"wood","subclass":"wood"}
    }    
    """.trimIndent()

    val featurePolygon = Feature.fromJson(featurePolygonJson)

}

/*
    Feature{
        type=Feature,
        bbox=null,
        id=13499067,
        geometry=Polygon{
            type=MultiPolygon,
            bbox=null,
            coordinates=[
                [
                    [
                        Point{type=Point, bbox=null, coordinates=[133.7695848941803, 34.59703710033919]},
                        Point{type=Point, bbox=null, coordinates=[133.76948833465576, 34.597015021310796]},
                        ...
                    ]
                ],
                [
                    [
                        Point{type=Point, bbox=null, coordinates=[133.76950174570084, 34.596970863236365]},
                        Point{type=Point, bbox=null, coordinates=[133.7695661187172, 34.59697969485315]},
                        ...
                    ]
                :


     */