package com.example.receipeapp

data class Meal(val image:String ?= null, val title:String ?= null, val calories:String ?= null, val ingredients:List<String> ?= emptyList(),
val category:Map<String,Boolean> ?= emptyMap(), val instructions:List<String> ?= emptyList(), val url:String ?= null){

}