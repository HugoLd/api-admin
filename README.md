# BAMBY

Descriptif du projet :

Le projet BAMBY sert à gérer les humeurs d'une équipe projet , à la manière de TEAM MOOD , il permet après configuration de créer des projets , d'y affecter des participants , et de gérer les envois de mails réguliers permettant de connaitre leurs humeurs.

La partie affichage permet à l'utilisateur de noter son humeur et de la commenter , ainsi que de consulter les statistiques et de gérer les projets , la partie API permet le traitement et la gestion de la persistence des données.

________________________________________________________________________________________________________________________________
________________________________________________________________________________________________________________________________

Outils utilisés :

DB :
- MongoDB

Server :
- Java
- Spring ( Mail , Data , MVC ...)
- Maven

Client :
- Javascript
- JQuery
- C3.js ( Graphiques )
- DataTables ( Tableaux )

________________________________________________________________________________________________________________________________
________________________________________________________________________________________________________________________________

Descriptif partie java :

Partie Java -> src  
________________________________________________________________________________________________________________________________
  -Package org.cap.bean 
  
    -> objet Mood > objet stockant une humeur du jour , peut être manipulé dans mongodb par Spring data
	
    -> objet Project > objet stockant les informations d'un projet , peut être manipulé dans mongodb par Spring data
	
    -> enum MoodValue > stocke l'humeur correspondant à chaque valeurs
    
________________________________________________________________________________________________________________________________	
  -Package org.cap.controller
  
    -> controleur Projects > redirige les requêtes sur les services correspondants
	
    - Bean > objets I/O manipulés par le controleur
	
          -> AddMoodInput > objet d'entrée pour ajouter un mood
		  
          -> AddProjectInput > objet d'entrée pour ajouter un projet
		  
          -> AddUserToProjectInput > objet d'entrée pour ajouter une adresse mail à la liste d'un projet
		  
          -> ErrorOutPut > objet de sortie pour renvoyer une erreur
	  
________________________________________________________________________________________________________________________________  
  -Package org.cap.repo
      -> interface Repo > interface globale des classes de persistences , contient les opérations simples de persistence
      -> classe MoodRepoImplMongo > classe de persistence en base mongodb concernant les objets Mood , implémente Repo
      -> classe ProjectrepoImplMongo > classe de persistence en base mongodb concernant les objets Project , implémente Repo
      
________________________________________________________________________________________________________________________________
  -Package org.cap.service
      -> classe MailService > classe permettant le traitement des actions de mailing
      -> classe MoodService > classe permettant le traitement des actions concernant les humeurs
      -> classe projectService > classe permettant le traitement des actions concernant les projets
      
________________________________________________________________________________________________________________________________
  -Package org.cap.utils
      -> classe Util > classe utilitaire permettant des actions récurrentes dans différentes classes ( date , uuid ..) 
      
________________________________________________________________________________________________________________________________
  -Ressources
     -Properties
     	-> mail.properties > permet de configurer les préferences d'envoi de mail
	-> mongoConfig.properties > permet de configurer les préferences d'accès à la base mongoDB
     -mailTemplate
     	->mailTemplate > configuration du contenu de l'e-mail envoyé

________________________________________________________________________________________________________________________________
________________________________________________________________________________________________________________________________

Partie JavaScript -> front
