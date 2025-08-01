package net.uoneweb.android.til.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import net.uoneweb.android.til.R
import net.uoneweb.android.til.ui.chatsample.Message
import net.uoneweb.android.til.ui.chatsample.SampleData
import net.uoneweb.android.til.ui.theme.MyApplicationTheme
import javax.inject.Inject

class ChatSampleFragment
    @Inject
    constructor() : Fragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
        }

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View {
            return ComposeView(requireContext()).apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    MyApplicationTheme {
                        Conversation(SampleData.conversationSample)
                    }
                }
            }
        }

        @Composable
        fun Conversation(messages: List<Message>) {
            LazyColumn {
                items(messages) { message ->
                    MessageCard(message)
                }
            }
        }

        @Preview
        @Composable
        fun PreviewConversation() {
            MyApplicationTheme {
                Surface {
                    Conversation(SampleData.conversationSample)
                }
            }
        }

        @Composable
        fun MessageCard(msg: Message) {
            Row(modifier = Modifier.padding(all = 8.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "icon",
                    modifier =
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape),
                )

                Spacer(modifier = Modifier.width(8.dp))

                var isExpanded by remember { mutableStateOf(false) }
                val surfaceColor by animateColorAsState(
                    if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                )

                Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
                    Text(
                        text = msg.author,
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = surfaceColor,
                        shadowElevation = 1.dp,
                        modifier =
                            Modifier
                                .animateContentSize()
                                .padding(1.dp),
                    ) {
                        Text(
                            text = msg.body,
                            modifier = Modifier.padding(all = 4.dp),
                            maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }

        @Preview(name = "Light Mode")
        @Preview(
            uiMode = Configuration.UI_MODE_NIGHT_YES,
            showBackground = true,
            name = "Dark Mode",
        )
        @Composable
        fun PreviewMessageCard() {
            MyApplicationTheme {
                Surface {
                    MessageCard(Message("test1", "test2"))
                }
            }
        }
    }
