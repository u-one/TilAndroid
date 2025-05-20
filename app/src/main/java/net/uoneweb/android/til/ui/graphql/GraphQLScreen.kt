package net.uoneweb.android.til.ui.graphql

//import com.apollographql.apollo3.ApolloClient
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
//import com.trevorblades.countries.GetCountryQuery
import kotlinx.coroutines.launch

@Composable
fun GraphQLScreen() {
    var code by remember { mutableStateOf("ZW") }
    //var country by remember { mutableStateOf<GetCountryQuery.Country?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(code) {
        scope.launch {
            // TODO: fix dependencies
            /*
            val appolloclient =
                ApolloClient.Builder()
                    .serverUrl("https://countries.trevorblades.com/graphql")
                    .build()
            val response = appolloclient.query(GetCountryQuery(code)).execute()
            country = response.data?.country
            Log.d("GraphQLScreen", "Response: $country")
             */
        }
    }

    Column {
        Text("TODO: Fix dependencies")
        Text("GraphQL Screen")
        Text("code: $code")
        /*
        country?.let {
            Text("Country: ${it.name}")
            Text("Native: ${it.native}")
            Text("Currency: ${it.currency}")
        }
         */
    }
}
