## 📔 Logbog 

### 🧩 US-1: Systemadministrator – Database og entiteter

> _"Jeg fik lavet entiterne og besluttet mig for at bruge en ManyToMany relation da der i opgaven stod "Each candidate can have many skills, and each skill can belong to many candidates." Jeg har derudover besluttet mig for at lave et endpoint til at populate fordi det er nemmere"_

✅ Entiteter og relationer fungerer  
✅ Istedet for en populator klasse er der lavet et endpoint

### 🛠️ US-2: Developer – DAO og DTO-lag

> _"Jeg implementerede DAO-klasserne for både Candidate og Skill. CRUD-metoderne blev testet med data. DTO-klasser blev brugt konsekvent for at sikre separation mellem lagene. Det gav en renere struktur og gjorde det nemmere at kontrollere"_

✅ DAO-lag med CRUD  
✅ DTOs mellem lagene

### 🌐 US-3: API – REST-endpoints

> _"Jeg byggede REST-endpoints med Javalin og testede dem med dev.http filen og Rest Assured. Det var vigtigt at sikre korrekt statuskode og JSON-format ved både succes og fejl. Jeg implementerede linking mellem candidate og skill via en PUT-endpoint."_

✅ GET /candidates  
✅ GET /candidates/{id} 
✅ POST, PUT, DELETE for candidates  
✅ PUT /candidates/{candidateId}/skill/{skillId}

### 🔍 US-4: Recruiter – Filtrering

> _"Jeg tilføjede en query-parameter til GET /candidates, så man kan filtrere på kategori. Jeg fandt ud af at man skulle bruge readall metoden og lave funktionalitet i den som tjekkede om der var en kategori med."_

✅ GET /candidates?category={category}


### 📊 US-5: Recruiter – Candidate Popularity score

>_"Når man skal finde en specifik kandidat bliver der fetched fra api'et og hentet hvilke skills kandidaten har og kommer med i responset som en "SkillListResponseDTO" "_

✅ GET /candidates/{id}

###  US-6: Analyst – Skills API-integration

>_"Jeg lavede en endpoint, som tog candidate id og popularityScore. Derfor lavede jeg et PopularityResponseDTO til dette."_

✅ GET {{url}}/reports/candidates/top-by-popularity

### 🧪 US-7: Tester – REST Endpoints test

>_"Jeg skrev tests med JUnit og Rest Assured. Hver endpoint blev testet for både succes og fejlscenarier. Det var vigtigt at rydde op i testdata mellem tests."_

✅ Test for alle endpoints  

### 🔐 US-8: Security – JWT og adgangskontrol

> _"Jeg implementerede JWT-login og sikrede alle endpoints undtagen login, register og populate. Rollebaseret adgang blev tilføjet, så kun user kan gøre noget da det er en del af opgavens krav."_

✅ POST /login returnerer JWT  
✅ Rollebaseret adgang  
✅ 401 ved manglende eller ugyldig token  
✅ Tests for sikkerhedsscenarier