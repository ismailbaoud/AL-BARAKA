# AL-BARAKA

#### 5. Accéder à l'application

Ouvrez votre navigateur et accédez à : http://localhost:8081/login

**Compte de test Agent Bancaire (via Keycloak)** :
- Username : `test`
- Password : `test`

---

### Option 2 : Démarrage en mode développement (sans Docker)

#### 1. Installer et démarrer PostgreSQL

Créez une base de données :

```sql
CREATE DATABASE albaraka_dev;
```

#### 2. Installer et démarrer Keycloak

```bash
# Télécharger Keycloak 24.0
wget https://github.com/keycloak/keycloak/releases/download/24.0.0/keycloak-24.0.0.tar.gz
tar -xzf keycloak-24.0.0.tar.gz
cd keycloak-24.0.0

# Démarrer en mode dev
bin/kc.sh start-dev
```

Importer le realm : `keycloak-imports/albaraka-realm.json` via l'interface admin.

#### 3. Configurer le fichier .env

Utilisez les variables `DEV_*` dans votre `.env` :

```bash
SPRING_PROFILES_ACTIVE=dev
DEV_DATABASE=albaraka_dev
DEV_DB_USERNAME=postgres
DEV_DB_PASSWORD=postgres
JWT_ISSUER_URI=http://localhost:8080/realms/albaraka
JWT_JWK_SET_URI=http://localhost:8080/realms/albaraka/protocol/openid-connect/certs
```

#### 4. Lancer l'application

```bash
./mvnw spring-boot:run
```

L'application sera disponible sur : http://localhost:8082

---

## 📖 Utilisation

### Créer un compte client

**Endpoint** : `POST /auth/register`

```json
{
  "fullName": "John Doe",
  "email": "john@example.com",
  "password": "securePassword123"
}
```

**Réponse** : Retourne un objet `AccountResponse` avec le numéro de compte généré.

### Se connecter

**Interface Web** : http://localhost:8081/login

Ou via **API** : `POST /auth/login`

```json
{
  "fullName": "John Doe",
  "password": "securePassword123"
}
```

### Effectuer une opération (Client)

**Endpoint** : `POST /api/client/operations`

```json
{
  "operationType": "DEPOSIT",
  "amount": 5000.0
}
```

**Statuts possibles** :
- `APPROVED` : Opération < 10 000 DH, exécutée immédiatement
- `PANDING` : Opération ≥ 10 000 DH, en attente de validation

### Upload de document justificatif

**Endpoint** : `POST /api/client/operations/{operationId}/document`

**Type** : `multipart/form-data`

**Paramètres** :
- `file` : Fichier (PDF, JPG, PNG, max 5 MB)

### Valider une opération (Agent)

**Approuver** : `PUT /api/agent/operations/{id}/approve`

**Rejeter** : `PUT /api/agent/operations/{id}/reject`

### Exemples avec curl

```bash
# Créer un compte
curl -X POST http://localhost:8081/auth/register \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","email":"test@example.com","password":"test123"}'

# Connexion
curl -X POST http://localhost:8081/auth/login \
  -H "Content-Type: application/json" \
  -d '{"fullName":"Test User","password":"test123"}'

# Créer un dépôt
curl -X POST http://localhost:8081/api/client/operations \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{"operationType":"DEPOSIT","amount":5000}'
```

---

## 📁 Structure du projet

```
al-baraka/
├── src/
│   ├── main/
│   │   ├── java/com/ismail/al_baraka/
│   │   │   ├── config/              # Configuration Spring Security, JWT, OAuth2
│   │   │   ├── controller/          # Contrôleurs REST et MVC
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── Exception/           # Gestion des exceptions
│   │   │   ├── helper/              # Utilitaires (génération numéro compte, etc.)
│   │   │   ├── mapper/              # MapStruct mappers
│   │   │   ├── model/               # Entités JPA
│   │   │   │   └── enums/           # Énumérations (Role, Status, OperationType)
│   │   │   ├── repository/          # Repositories JPA
│   │   │   └── service/             # Services métier
│   │   └── resources/
│   │       ├── application*.properties  # Configuration par profil
│   │       ├── templates/           # Templates Thymeleaf
│   │       └── static/              # Assets statiques
│   └── test/                        # Tests unitaires et d'intégration
├── keycloak-imports/                # Configuration Keycloak
├── upload/                          # Dossier de stockage des documents
├── docker-compose.yml               # Orchestration Docker
├── Dockerfile                       # Image Docker de l'application
├── pom.xml                          # Dépendances Maven
└── README.md                        # Ce fichier
```

---

## 🚀 Déploiement et Hébergement

### Infrastructure Cloud - DigitalOcean

Ce projet est **hébergé sur DigitalOcean**, une plateforme cloud reconnue pour sa fiabilité et sa simplicité d'utilisation. Le déploiement en environnement de production a été réalisé avec succès sur un serveur Droplet Linux.

### 🛠️ Processus de Déploiement

Le déploiement de cette plateforme bancaire a impliqué plusieurs étapes techniques complexes, offrant une expérience d'apprentissage approfondie dans l'administration système et le DevOps :

#### 1. **Configuration du Serveur**
- **Provisionnement** : Création et configuration d'un Droplet Ubuntu 22.04 LTS
- **Sécurisation** : Configuration des utilisateurs non-root avec privilèges sudo
- **Mise à jour système** : Application des dernières mises à jour de sécurité
- **Installation des dépendances** : Java 21, PostgreSQL 15, Docker, Docker Compose

#### 2. **Accès et Gestion via SSH**
- **Connexion sécurisée** : Établissement de connexions SSH avec authentification par clé
- **Gestion des clés SSH** : Génération et configuration de paires de clés publique/privée
- **Configuration SSH** : Personnalisation du fichier `~/.ssh/config` pour un accès simplifié
- **Transfert de fichiers** : Utilisation de SCP et SFTP pour le déploiement des artifacts

#### 3. **Configuration Nginx comme Reverse Proxy**
- **Installation et configuration** : Nginx pour gérer le trafic HTTP/HTTPS
- **Reverse proxy** : Redirection du trafic vers l'application Spring Boot (port 8081)
- **Certificats SSL/TLS** : Configuration de HTTPS avec Let's Encrypt
- **Optimisation** : Configuration de la compression gzip et du caching
- **Logs** : Mise en place de la rotation des logs et monitoring

**Exemple de configuration Nginx** :
```nginx
server {
    listen 80;
    server_name votre-domaine.com;

    location / {
        proxy_pass http://localhost:8081;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

#### 4. **Gestion du Firewall (UFW)**
- **Configuration des règles** : Ouverture sélective des ports nécessaires
  - Port 22 : SSH
  - Port 80 : HTTP
  - Port 443 : HTTPS
- **Sécurité renforcée** : Blocage de tous les autres ports par défaut
- **Rate limiting** : Protection contre les attaques DDoS et brute force

**Commandes UFW utilisées** :
```bash
sudo ufw allow OpenSSH
sudo ufw allow 'Nginx Full'
sudo ufw enable
sudo ufw status
```

#### 5. **Déploiement de l'Application**
- **Build de production** : Compilation du WAR avec `mvnw clean package`
- **Transfert sur le serveur** : Upload via SCP
- **Configuration des variables d'environnement** : Fichier `.env` pour la production
- **Orchestration Docker** : Déploiement avec Docker Compose
- **Gestion des services** : Configuration de systemd pour le démarrage automatique

#### 6. **Base de Données et Keycloak**
- **PostgreSQL** : Installation et configuration avec utilisateurs dédiés
- **Keycloak** : Déploiement du serveur d'authentification en conteneur Docker
- **Import du realm** : Configuration automatique via `keycloak-imports/albaraka-realm.json`
- **Backup automatique** : Mise en place de scripts de sauvegarde journalière

### 📚 Compétences Acquises

Ce processus de déploiement a permis de développer des compétences essentielles en :

| Domaine | Compétences |
|---------|-------------|
| **Administration Linux** | Gestion d'un serveur Ubuntu, commandes bash, gestion des processus et services |
| **Sécurité** | Configuration SSH, gestion du firewall UFW, certificats SSL/TLS, sécurisation des accès |
| **DevOps** | Déploiement continu, gestion de configurations, automatisation avec scripts shell |
| **Réseau** | Configuration Nginx, reverse proxy, gestion des ports, DNS |
| **Conteneurisation** | Docker, Docker Compose, gestion d'images et de volumes |
| **Monitoring** | Analyse des logs, surveillance des performances, résolution d'incidents |

### 🔒 Bonnes Pratiques Implémentées

- ✅ **Séparation des environnements** : Profils distincts pour dev/prod
- ✅ **Gestion des secrets** : Variables d'environnement et fichiers `.env` sécurisés
- ✅ **HTTPS obligatoire** : Chiffrement de toutes les communications
- ✅ **Firewall configuré** : Surface d'attaque minimale
- ✅ **Logs centralisés** : Facilite le débogage et l'audit
- ✅ **Mises à jour régulières** : Système et dépendances à jour

### 🎓 Retour d'Expérience

Le déploiement de cette application sur DigitalOcean a représenté un **véritable défi technique** et une **opportunité d'apprentissage exceptionnelle**. Au-delà du développement applicatif, ce projet a permis de :

- Comprendre les **enjeux de production** : haute disponibilité, sécurité, performances
- Maîtriser les **outils d'administration système** : SSH, Nginx, UFW, systemd
- Appréhender les **problématiques réseau** : DNS, ports, protocoles
- Développer une **approche DevOps** : automatisation, monitoring, déploiement continu
- Renforcer la **sécurité applicative** : HTTPS, firewall, authentification robuste

Cette expérience pratique a consolidé la compréhension du **cycle de vie complet** d'une application moderne, de la conception au déploiement en production.

---

## Tests

### Exécuter les tests

```bash
# Tous les tests
./mvnw test

# Tests d'une classe spécifique
./mvnw test -Dtest=UserServiceTest

# Avec couverture de code
./mvnw clean test jacoco:report
```

### Types de tests

Le projet inclut :
- **Tests unitaires** : Services, mappers, utilitaires
- **Tests d'intégration** : Repositories, contrôleurs
- **Tests de sécurité** : Authentification, autorisation

---

## Licence

Ce projet est distribué sous la licence **GNU General Public License v3.0 (GPL-3.0)**.

Vous êtes libre de :
- Utiliser ce logiciel à des fins commerciales
- Modifier le code source
- Distribuer des copies
- Utiliser ce logiciel en privé

**Conditions** :
- Divulguer le code source des modifications
- Inclure la licence et les droits d'auteur
- Indiquer les changements effectués
- Utiliser la même licence (GPL-3.0) pour les dérivés

Voir le fichier [LICENSE](LICENSE) pour plus de détails.

---

## 👥 Crédits

### Développeur principal

- **Ismail** - Développement initial et architecture

### Technologies et frameworks

Ce projet s'appuie sur des technologies open source de qualité :

- [Spring Framework](https://spring.io/) - Framework Java entreprise
- [Keycloak](https://www.keycloak.org/) - Solution IAM open source
- [PostgreSQL](https://www.postgresql.org/) - Base de données relationnelle
- [MapStruct](https://mapstruct.org/) - Générateur de mappers Java
- [Lombok](https://projectlombok.org/) - Réduction du boilerplate Java
- [Thymeleaf](https://www.thymeleaf.org/) - Moteur de templates
- [Tailwind CSS](https://tailwindcss.com/) - Framework CSS utilitaire

### Ressources et inspirations

- [Spring Security OAuth2 Documentation](https://docs.spring.io/spring-security/reference/servlet/oauth2/index.html)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)

---

## Support et contact

Pour toute question ou suggestion :
- **Bugs** : Ouvrir une issue sur GitHub
- **Améliorations** : Proposer une pull request
- **Contact** : [Votre email]

---

<div align="center">

**Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile !**

Développé avec passion par Ismail

</div>
