# Développement Android

## Laboratoire n°2: Interactions avec l’utilisateur - Approche MVC

### Friedli Jonathan, Marengo Stéphane, Silvestri Géraud

### 04.11.2022

## Introduction
Le but de ce laboratoire est de réaliser une
activité proposant un formulaire permettant d’éditer les informations d’une personne. Ledit formulaire est décomposé en plusieurs parties et devra s'adapter à la saisie d'un étudiant ou d'un travailleur en affichant uniquement les parties pertinentes.

## 1. Question complémentaire
### 1.1 

**Pour le champ remark, destiné à accueillir un texte pouvant être plus long qu’une seule ligne, quelle configuration particulière faut-il faire dans le fichier XML pour que son comportement soit correct ? Nous pensons notamment à la possibilité de faire des retours à la ligne, d’activer le correcteur orthographique et de permettre au champ de prendre la taille nécessaire.**

Il faut mettre 
```xml
android:inputType="text|textMultiLine|textCapSentences|textAutoCorrect"
```
afin de permettre le retour à la ligne et d'activer le correcteur orthographique.

### 1.2

**Pour afficher la date sélectionnée via le DatePicker nous pouvons utiliser un DateFormat permettant par exemple d’afficher 12 juin 1996 à partir d’une instance de Date. Le formatage des dates peut être relativement différent en fonction des langues, la traduction des mois par exemple, mais également des habitudes régionales différentes : la même date en anglais britannique serait 12th June 1996 et en anglais américain June 12, 1996. Comment peut-on gérer cela au mieux ?**

## 1.3

**Est-il possible de limiter les dates sélectionnables dans le dialogue, en particulier pour une date de naissance il est peu probable d’avoir une personne née il y a plus de 110 ans ou à une date dans le futur. Comment pouvons-nous mettre cela en place ?**

## 1.4

**Lors du remplissage des champs textuels, vous pouvez constater que le bouton « suivant » présent sur le clavier virtuel permet de sauter automatiquement au prochain champ à saisir, cf. Fig. 2. Est-ce possible de spécifier son propre ordre de remplissage du questionnaire ? Arrivé sur le dernier champ, est-il possible de faire en sorte que ce bouton soit lié au bouton de validation du questionnaire ?**

## 1.5

**Pour les deux Spinners (nationalité et secteur d’activité), comment peut-on faire en sorte que le premier choix corresponde au choix null, affichant par exemple « Sélectionner » ? Comment peut-on gérer cette valeur pour ne pas qu’elle soit confondue avec une réponse ?**

Il faut ajouter une valeur et la mettre en permier dans le spinner. Ensuite, on met une taille de 0 pour cette première valeur que l'utilisateur ne pense pas pouvoir la sélectionner.

https://stackoverflow.com/questions/4726490/how-to-set-spinner-default-value-to-null