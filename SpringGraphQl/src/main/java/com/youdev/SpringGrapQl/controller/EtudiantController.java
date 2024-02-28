package com.youdev.SpringGrapQl.controller;

import com.youdev.SpringGrapQl.dao.EtudiantRepository;
import com.youdev.SpringGrapQl.entity.Etudiant;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
public class EtudiantController {

	@Autowired
	private EtudiantRepository repository;

	@Value("classpath:etudiant.graphqls")
	private Resource schemaResource;

	private GraphQL graphQL;

	@PostConstruct
	public void loadSchema() throws IOException {
		File schemaFile = schemaResource.getFile();
		TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
		RuntimeWiring wiring = buildWiring();
		GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
		graphQL = GraphQL.newGraphQL(schema).build();
	}

	private RuntimeWiring buildWiring() {
		DataFetcher<List<Etudiant>> fetcher1 = data -> {
			return (List<Etudiant>) repository.findAll();
		};

		DataFetcher<Etudiant> fetcher2 = data -> {
			return repository.findByEmail(data.getArgument("email"));
		};

		return RuntimeWiring.newRuntimeWiring().type("Query",
				typeWriting -> typeWriting.dataFetcher("getAllEtudiant", fetcher1).dataFetcher("findPerson", fetcher2))
				.build();

	}

	@PostMapping("/addEtudiant")
	public String addEtudiant(@RequestBody List<Etudiant> etudiants) {
		repository.saveAll(etudiants);
		return "Records inserted: " + etudiants.size();
	}

	@GetMapping("/findAllEtudiant")
	public List<Etudiant> getPersons() {
		return (List<Etudiant>) repository.findAll();
	}

	@PostMapping("/getAll")
	public ResponseEntity<Object> getAll(@RequestBody String query) {
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

	@PostMapping("/getEtudiantByEmail")
	public ResponseEntity<Object> getEtudiantByEmail(@RequestBody String query) {
		ExecutionResult result = graphQL.execute(query);
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}

}
