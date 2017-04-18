# api-admin

_________________________________________
Partie Java -> src  

Partie JavaScript -> front
_________________________________________

Descriptif partie java :

  -Package org.cap.Bean 
  
    -> Objet Mood > objet stockant une humeur du jour , peut être manipulé dans mongodb par Spring data
	
    -> Objet Project > objet stockant les informations d'un projet , peut être manipulé dans mongodb par Spring data
	
    -> Enum MoodValue > stocke l'humeur correspondant à chaque valeurs
    
	
  -Package org.cap.controller
  
    -> Controleur Projects > redirige les requêtes sur les services correspondants
	
    - Bean > objets I/O manipulés par le controleur
	
          -> AddMoodInput > objet d'entrée pour ajouter un mood
		  
          -> AddProjectInput > objet d'entrée pour ajouter un projet
		  
          -> AddUserToProjectInput > objet d'entrée pour ajouter une adresse mail à la liste d'un projet
		  
          -> ErrorOutPut > objet de sortie pour renvoyer une erreur
