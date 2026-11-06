package vn.nhauonline.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Orange = Color(0xFFB85B1E)
private val Cream = Color(0xFFFFF8ED)
private val Ink = Color(0xFF39291F)
private val Muted = Color(0xFF8A7868)
private val Gold = Color(0xFFF6D49B)

data class PubRoom(
    val id: Int,
    val name: String,
    val members: Int,
    val capacity: Int,
    val mood: String,
    val locked: Boolean = false
)

private val sampleRooms = listOf(
    PubRoom(1, "Hội anh em", 4, 6, "Vui vẻ"),
    PubRoom(2, "Giao lưu vui vẻ", 3, 8, "Kết bạn"),
    PubRoom(3, "Tâm sự đêm nay", 2, 4, "Tâm sự"),
    PubRoom(4, "Chiến hữu miền Tây", 7, 10, "Sôi động"),
    PubRoom(5, "Bàn làm quen", 1, 6, "Mở cửa")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NhauOnlineApp() }
    }
}

@Composable
fun NhauOnlineApp() {
    var tab by remember { mutableStateOf("Trang chủ") }
    var selectedRoom by remember { mutableStateOf<PubRoom?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var rooms by remember { mutableStateOf(sampleRooms) }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Orange,
            secondary = Color(0xFFE9A23B),
            background = Cream,
            surface = Color.White,
            onBackground = Ink,
            onSurface = Ink
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = Cream) {
            when {
                selectedRoom != null -> RoomScreen(
                    room = selectedRoom!!,
                    onBack = { selectedRoom = null }
                )
                else -> Scaffold(
                    containerColor = Cream,
                    bottomBar = {
                        NavigationBar(containerColor = Color.White) {
                            val items = listOf(
                                "Trang chủ" to Icons.Default.Home,
                                "Phòng nhậu" to Icons.Default.SportsBar,
                                "Cửa hàng" to Icons.Default.Storefront,
                                "Bạn bè" to Icons.Default.People,
                                "Cá nhân" to Icons.Default.Person
                            )
                            items.forEach { (label, icon) ->
                                NavigationBarItem(
                                    selected = tab == label,
                                    onClick = { tab = label },
                                    icon = { Icon(icon, contentDescription = label) },
                                    label = { Text(label, fontSize = 10.sp) }
                                )
                            }
                        }
                    }
                ) { padding ->
                    Box(Modifier.padding(padding)) {
                        when (tab) {
                            "Trang chủ" -> HomeScreen(
                                rooms = rooms,
                                onCreate = { showCreate = true },
                                onJoin = { selectedRoom = it },
                                onSeeRooms = { tab = "Phòng nhậu" }
                            )
                            "Phòng nhậu" -> RoomsScreen(rooms, onJoin = { selectedRoom = it }, onCreate = { showCreate = true })
                            "Cửa hàng" -> ShopScreen()
                            "Bạn bè" -> FriendsScreen()
                            else -> ProfileScreen()
                        }
                    }
                }
            }

            if (showCreate) {
                CreateRoomDialog(
                    onDismiss = { showCreate = false },
                    onCreate = { name, capacity ->
                        val newRoom = PubRoom(
                            id = (rooms.maxOfOrNull { it.id } ?: 0) + 1,
                            name = name,
                            members = 1,
                            capacity = capacity,
                            mood = "Bàn mới"
                        )
                        rooms = listOf(newRoom) + rooms
                        showCreate = false
                        selectedRoom = newRoom
                    }
                )
            }
        }
    }
}

@Composable
private fun HomeScreen(
    rooms: List<PubRoom>,
    onCreate: () -> Unit,
    onJoin: (PubRoom) -> Unit,
    onSeeRooms: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text("🍻 NHẬU ONLINE", color = Orange, fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
                    Text("Vào bàn là có bạn!", color = Muted, fontSize = 12.sp)
                }
                IconButton(onClick = {}) { Icon(Icons.Default.NotificationsNone, contentDescription = "Thông báo", tint = Orange) }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Avatar("😎", 58)
                Column(Modifier.weight(1f)) {
                    Text("Xin chào, Chiến hữu!", fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    Text("Tối nay có bàn nào vui không?", color = Muted, fontSize = 12.sp)
                }
                AssistChip(onClick = {}, label = { Text("Online") }, leadingIcon = { Icon(Icons.Default.Circle, null, tint = Color(0xFF36A66A), modifier = Modifier.size(9.dp)) })
            }
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Gold),
                shape = RoundedCornerShape(22.dp),
                modifier = Modifier.fillMaxWidth().clickable { onCreate() }
            ) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("🍺 Mở bàn nhậu thôi!", fontWeight = FontWeight.ExtraBold, fontSize = 21.sp, color = Color(0xFF783B16))
                    Text("Rủ bạn bè hoặc kết nối chiến hữu mới.", color = Color(0xFF783B16), fontSize = 13.sp)
                    Button(onClick = onCreate, colors = ButtonDefaults.buttonColors(containerColor = Orange)) {
                        Icon(Icons.Default.Add, null)
                        Spacer(Modifier.width(6.dp))
                        Text("TẠO BÀN NHẬU")
                    }
                }
            }
        }
        item {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text("🔥 Bàn đang hoạt động", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                TextButton(onClick = onSeeRooms) { Text("Xem tất cả") }
            }
        }
        items(rooms.take(4)) { room -> RoomCard(room, onClick = { onJoin(room) }) }
        item { Text("✨ Mẹo nhỏ: Chọn một bàn còn chỗ để làm quen bạn mới.", color = Muted, fontSize = 12.sp) }
    }
}

@Composable
private fun RoomsScreen(rooms: List<PubRoom>, onJoin: (PubRoom) -> Unit, onCreate: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Phòng nhậu 🍻", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
            Button(onClick = onCreate) { Icon(Icons.Default.Add, null); Text("Tạo bàn") }
        }
        Text("Chọn một chiếc bàn còn chỗ để tham gia.", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(12.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(rooms) { RoomCard(it, onClick = { onJoin(it) }) }
        }
    }
}

@Composable
private fun RoomCard(room: PubRoom, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(enabled = room.members < room.capacity) { onClick() },
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(48.dp).clip(RoundedCornerShape(14.dp)).background(Color(0xFFFFE6C4)), contentAlignment = Alignment.Center) {
                Text("🍻", fontSize = 25.sp)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(room.name, fontWeight = FontWeight.Bold)
                Text("${room.mood} • ${room.members}/${room.capacity} người", color = Muted, fontSize = 12.sp)
            }
            if (room.members < room.capacity) {
                Text("Vào bàn →", color = Orange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            } else {
                Text("Đã đầy", color = Muted, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun RoomScreen(room: PubRoom, onBack: () -> Unit) {
    var message by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf("🍻 Chào mừng mọi người đến ${room.name}!", "😎 Tối nay vui vẻ nhé!")) }
    Column(Modifier.fillMaxSize().background(Color(0xFFFFF3DF))) {
        Row(Modifier.fillMaxWidth().background(Color.White).padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
            Column(Modifier.weight(1f)) {
                Text(room.name, fontWeight = FontWeight.Bold)
                Text("${room.members}/${room.capacity} chỗ • Bàn công khai", color = Muted, fontSize = 11.sp)
            }
            IconButton(onClick = {}) { Icon(Icons.Default.MoreVert, null) }
        }
        Box(Modifier.weight(1f).fillMaxWidth().padding(12.dp), contentAlignment = Alignment.Center) {
            // Top-down virtual table illustration; multiplayer sync is added with the backend.
            Box(
                Modifier.fillMaxWidth(0.72f).aspectRatio(1.15f).clip(RoundedCornerShape(100))
                    .background(Color(0xFFB87945)),
                contentAlignment = Alignment.Center
            ) {
                Box(Modifier.fillMaxWidth(0.72f).aspectRatio(1f).clip(CircleShape).background(Color(0xFFDEAA70)), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🍺  🍢  🍤", fontSize = 25.sp)
                        Text("BÀN NHẬU", fontWeight = FontWeight.ExtraBold, color = Color(0xFF75421F), fontSize = 17.sp)
                        Text("Cùng vui nào!", color = Color(0xFF75421F), fontSize = 11.sp)
                    }
                }
            }
            AvatarSeat("😎", "Bạn", Modifier.align(Alignment.TopCenter).offset(y = 2.dp))
            AvatarSeat("😁", "Minh", Modifier.align(Alignment.CenterStart).offset(x = 2.dp))
            AvatarSeat("🤠", "Tuấn", Modifier.align(Alignment.CenterEnd).offset(x = (-2).dp))
            AvatarSeat("🥳", "Lan", Modifier.align(Alignment.BottomCenter).offset(y = (-2).dp))
        }
        Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("🍻", "😂", "❤️", "👏", "🎉").forEach { emoji ->
                FilledTonalButton(onClick = { messages = messages + "Bạn: $emoji" }, contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)) { Text(emoji, fontSize = 18.sp) }
            }
        }
        Card(Modifier.fillMaxWidth().padding(12.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(Modifier.padding(12.dp)) {
                Text("Trò chuyện", fontWeight = FontWeight.Bold)
                messages.takeLast(4).forEach { Text(it, fontSize = 12.sp, modifier = Modifier.padding(top = 5.dp), color = Ink) }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = message, onValueChange = { message = it },
                        modifier = Modifier.weight(1f), placeholder = { Text("Nhập lời chúc...") },
                        singleLine = true
                    )
                    IconButton(onClick = {
                        if (message.isNotBlank()) {
                            messages = messages + "Bạn: ${message.trim()}"
                            message = ""
                        }
                    }) { Icon(Icons.Default.Send, contentDescription = "Gửi", tint = Orange) }
                }
            }
        }
    }
}

@Composable
private fun AvatarSeat(emoji: String, name: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Avatar(emoji, 52)
        Text(name, color = Ink, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
}

@Composable
private fun Avatar(emoji: String, size: Int) {
    Box(
        Modifier.size(size.dp).clip(CircleShape).background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = (size * 0.52).sp)
    }
}

@Composable
private fun CreateRoomDialog(onDismiss: () -> Unit, onCreate: (String, Int) -> Unit) {
    var name by remember { mutableStateOf("Bàn chiến hữu") }
    var capacity by remember { mutableIntStateOf(6) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Tạo bàn nhậu 🍻", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Tên bàn") }, singleLine = true)
                Text("Số lượng chỗ")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(2, 4, 6, 8, 10).forEach { count ->
                        FilterChip(selected = capacity == count, onClick = { capacity = count }, label = { Text("$count") })
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onCreate(name.trim(), capacity) }) { Text("Tạo bàn") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Hủy") } }
    )
}

@Composable
private fun ShopScreen() {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Cửa hàng 🍤", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
        Text("Món ngon cho cuộc vui thêm trọn vẹn.", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(14.dp))
        val products = listOf(
            "🦐" to "Hải sản tươi ngon",
            "🍗" to "Đồ nướng",
            "🍲" to "Các món lẩu",
            "🥜" to "Mồi nhậu",
            "🥤" to "Nước giải khát",
            "🍉" to "Trái cây"
        )
        products.forEach { (emoji, title) ->
            Card(Modifier.fillMaxWidth().padding(bottom = 9.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(emoji, fontSize = 30.sp)
                    Spacer(Modifier.width(14.dp))
                    Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Khám phá →", color = Orange, fontSize = 12.sp)
                }
            }
        }
        Text("Cửa hàng đang ở chế độ giới thiệu. Đặt món và thanh toán sẽ được kết nối ở giai đoạn tiếp theo.", color = Muted, fontSize = 11.sp)
    }
}

@Composable
private fun FriendsScreen() {
    Column(Modifier.fillMaxSize().padding(18.dp)) {
        Text("Bạn bè 👥", fontSize = 23.sp, fontWeight = FontWeight.ExtraBold)
        Text("Tìm chiến hữu và mời vào bàn nhậu.", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(16.dp))
        listOf("Minh Nguyễn" to "😎", "Tuấn Trần" to "🤠", "Lan Phạm" to "🥳").forEach { (name, emoji) ->
            Row(Modifier.fillMaxWidth().padding(vertical = 9.dp), verticalAlignment = Alignment.CenterVertically) {
                Avatar(emoji, 48)
                Spacer(Modifier.width(12.dp))
                Text(name, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                Text("Đang online", color = Color(0xFF29945D), fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(22.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(30.dp))
        Avatar("😎", 100)
        Spacer(Modifier.height(12.dp))
        Text("Chiến hữu mới", fontSize = 21.sp, fontWeight = FontWeight.ExtraBold)
        Text("Thành viên Nhậu Online", color = Muted, fontSize = 12.sp)
        Spacer(Modifier.height(24.dp))
        listOf("🎭  Chỉnh sửa Avatar", "✏️  Sửa hồ sơ", "🔔  Cài đặt thông báo", "🛡️  Quyền riêng tư", "❓  Trợ giúp").forEach {
            Card(Modifier.fillMaxWidth().padding(bottom = 9.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Text(it, Modifier.padding(16.dp), fontWeight = FontWeight.Medium)
            }
        }
    }
}
