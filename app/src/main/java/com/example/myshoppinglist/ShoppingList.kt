package com.example.myshoppinglist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp


@Composable
fun MyShoppingList(){

    var sItems by remember { mutableStateOf(listOf<ShoppingList>()) }
    var showDialog by remember { mutableStateOf(false) }
    var itermName by remember { mutableStateOf("") }
    var itermQuantity by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxSize()
    )
    {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){

            Text(text = "ShoppingList:", style = MaterialTheme.typography.headlineMedium)

            Button(
                onClick = { showDialog = true }
            )
            {
                Text(text = "Add Items")
            }
        }

        LazyColumn(modifier = Modifier.fillMaxSize()){
            items(sItems){
                item ->
                if(item.isEditing){
                    shoppingItemEditor(
                        item =item ,
                        onEditComplete = {
                           editedName,editedQuantity->
                           sItems = sItems.map { it.copy(isEditing = false) }
                           val editedItem = sItems.find { it.id==item.id }
                           editedItem?.let {
                               it.name = editedName
                               it.quantity = editedQuantity
                           }
                        }
                    )
                }else{
                    ShoppingListItem(
                        item = item,
                        onEditClick = {
                            sItems = sItems.map { it.copy(isEditing = it.id==item.id) }
                        },
                        onDelete = {
                            sItems = sItems - item
                        }
                    )
                }
            }
        }
    }
    Column ( verticalArrangement = Arrangement.Bottom){
        Text(text = "Developed by Piyush & Sayali")
    }
    
    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog=false },
            confirmButton = {
                Row (modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),horizontalArrangement = Arrangement.SpaceBetween){
                    Button(
                        onClick = {
                            if(itermName.isNotEmpty()){
                                var newIterm = ShoppingList(
                                    id = sItems.size+1,
                                    name = itermName,
                                    quantity = itermQuantity.toIntOrNull() ?: 1
                                )
                                itermName = ""
                                itermQuantity = ""
                                sItems = sItems + newIterm
                            }
                            itermQuantity = ""
                            showDialog = false
                        }
                    )
                    {
                        Text(text = "Add")
                    }

                    Button(
                        onClick = {
                            itermName = ""
                            itermQuantity = ""
                            showDialog = false
                        }
                    )
                    {
                        Text(text = "Cancel")
                    }
                }
            } ,
            title = { Text(text = "Add Shopping Item")},
            text = {
                Column {

                    OutlinedTextField(
                        value = itermName,
                        onValueChange = {itermName = it},
                        singleLine = true,
                        label = { Text(text = "Enter Iterm")}
                    )
                    OutlinedTextField(
                        value = itermQuantity,
                        onValueChange = {itermQuantity = it},
                        singleLine = true,
                        label = { Text(text = "Enter Quantity")}
                    )


                }
            }
        )
    }
    
}

data class ShoppingList(
    val id:Int,
    var name:String,
    var quantity:Int,
    var isEditing:Boolean = false
)

@Composable
fun shoppingItemEditor(item: ShoppingList,onEditComplete:(String,Int)->Unit){
    var editedName by remember{ mutableStateOf(item.name) }
    var editedQuantity by remember{ mutableStateOf(item.quantity.toString()) }
//    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .border(border = BorderStroke(2.dp, Color.DarkGray), shape = RoundedCornerShape(20)),
        horizontalArrangement = Arrangement.SpaceBetween
        ) {

            BasicTextField(value = editedName, onValueChange = {editedName=it}, modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
                textStyle = TextStyle(color = Color.Gray)
            )
            BasicTextField(value = editedQuantity, onValueChange = {editedQuantity=it}, modifier = Modifier
                .padding(8.dp)
                .wrapContentSize(),
                textStyle = TextStyle(color = Color.Gray)
            )

            Button(onClick = {
//                isEditing = false
                onEditComplete(editedName,editedQuantity.toIntOrNull() ?: 1)
            }, modifier = Modifier.padding(8.dp)) {
            Text(text = "Save")
            }

    }
}

@Composable
fun ShoppingListItem(
    item:ShoppingList,
    onEditClick:() -> Unit={ },
    onDelete:() -> Unit
)
{
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(border = BorderStroke(2.dp, Color.DarkGray), shape = RoundedCornerShape(20)),
            horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty:${item.quantity}", modifier = Modifier.padding(8.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            IconButton(onClick = { onEditClick() }) {
                Icon(Icons.Default.Edit, contentDescription = "Null")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Null")
            }

        }
    }
}