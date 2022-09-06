package com.telekom.datacorona.vaccinations;

import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Vaccinations {
 @Id
 Integer vaccine_id;
 // title: Interné id vakcíny z /api/vaccines
 Integer  region_id;
 //title: Interné id regiónu z regiónov z /api/regions alebo null. Hodnota null znamená, že dáta nie sú priradené žiadnemu kraju.
 String     id;
 // title: Interné id záznamu
 Integer dose1_count;
 //title: Počet podaných prvých dávok vakcín pre daný deň, kraj a typ vakcíny
 Integer dose2_count;
 //title: Počet podaných druhých dávok vakcín pre daný deň, kraj a typ vakcíny
 String updated_at;
 //string($date-time)  title: Čas poslednej aktualizácie záznamu (čas poslednej zmeny hodnoty niektorého z atribútov záznamu) example: 2020-01-13 12:34:56

 String published_on;
// string($date-time)   title: Deň, pre ktorý sú dáta záznamu publikované pre potreby štatistík example: 2020-01-13


 public Vaccinations(Integer vaccine_id, Integer region_id, String id, Integer dose1_count, Integer dose2_count, String updated_at, String published_on) {
  this.vaccine_id = vaccine_id;
  this.region_id = region_id;
  this.id = id;
  this.dose1_count = dose1_count;
  this.dose2_count = dose2_count;
  this.updated_at = updated_at;
  this.published_on = published_on;
 }

 public Vaccinations() {
 }

 public Integer getVaccine_id() {
  return vaccine_id;
 }

 public void setVaccine_id(Integer vaccine_id) {
  this.vaccine_id = vaccine_id;
 }

 public Integer getRegion_id() {
  return region_id;
 }

 public void setRegion_id(Integer region_id) {
  this.region_id = region_id;
 }

 public String getId() {
  return id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public Integer getDose1_count() {
  return dose1_count;
 }

 public void setDose1_count(Integer dose1_count) {
  this.dose1_count = dose1_count;
 }

 public Integer getDose2_count() {
  return dose2_count;
 }

 public void setDose2_count(Integer dose2_count) {
  this.dose2_count = dose2_count;
 }

 public String getUpdated_at() {
  return updated_at;
 }

 public void setUpdated_at(String updated_at) {
  this.updated_at = updated_at;
 }

 public String getPublished_on() {
  return published_on;
 }

 public void setPublished_on(String published_on) {
  this.published_on = published_on;
 }
}
