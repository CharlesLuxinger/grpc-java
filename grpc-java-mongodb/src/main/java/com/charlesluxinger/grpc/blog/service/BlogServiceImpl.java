package com.charlesluxinger.grpc.blog.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.proto.blog.Blog;
import com.proto.blog.BlogServiceGrpc.BlogServiceImplBase;
import com.proto.blog.CreateBlogRequest;
import com.proto.blog.CreateBlogResponse;
import com.proto.blog.DeleteBlogRequest;
import com.proto.blog.DeleteBlogResponse;
import com.proto.blog.FindAllBlogRequest;
import com.proto.blog.FindAllBlogResponse;
import com.proto.blog.ReadBlogRequest;
import com.proto.blog.ReadBlogResponse;
import com.proto.blog.UpdateBlogRequest;
import com.proto.blog.UpdateBlogResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;

public class BlogServiceImpl extends BlogServiceImplBase {

	private final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	private final MongoDatabase database = mongoClient.getDatabase("blog");
	private final MongoCollection<Document> collection = database.getCollection("blog");

	@Override
	public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {
		System.out.println("Received Create Blog Request");

		Blog blog = request.getBlog();

		Document document = new Document().append("authorId", blog.getAuthorId())
										  .append("title", blog.getTitle())
										  .append("content", blog.getContent());

		System.out.println("Inserting a new Blog...");
		collection.insertOne(document);

		String id = document.get("_id").toString();
		System.out.println("Inserted Blog id: " + id);

		CreateBlogResponse response = CreateBlogResponse.newBuilder()
														.setBlog(blog.toBuilder()
																	 .setId(id)
																	 .build())
														.build();


		responseObserver.onNext(response);
		responseObserver.onCompleted();
	}

	@Override
	public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {
		System.out.println("Received Read Blog Request");

		System.out.println("Searching for a Blog...");
		Document result = null;
		try {
			result = collection.find(eq("_id", new ObjectId(request.getBlogId()))).first();
		} catch (Exception ex){
			System.out.println("Blog id: " + request.getBlogId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
						  .withDescription("The blog with the corresponding id was not found.")
						  .augmentDescription(ex.getLocalizedMessage())
						  .asRuntimeException()
			);
		}

		if (result == null){
			System.out.println("Blog id: " + request.getBlogId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
						  .withDescription("The blog with the corresponding id was not found.")
						  .augmentDescription("Blog id: " + request.getBlogId() + " not found.")
						  .asRuntimeException()
					);
		}

		System.out.println("Blog found, sending response...");

		responseObserver.onNext(ReadBlogResponse.newBuilder()
												.setBlog(documentToBlog(result))
												.build());
		responseObserver.onCompleted();
	}

	@Override
	public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {
		System.out.println("Received Update Blog Request");

		System.out.println("Searching for a Blog to update...");
		Document result = null;
		try {
			result = collection.find(eq("_id", new ObjectId(request.getBlog().getId()))).first();
		} catch (Exception ex){
			System.out.println("Blog id: " + request.getBlog().getId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with the corresponding id was not found.")
							.augmentDescription(ex.getLocalizedMessage())
							.asRuntimeException()
			);
		}

		if (result == null){
			System.out.println("Blog id: " + request.getBlog().getId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with the corresponding id was not found.")
							.augmentDescription("Blog id: " + request.getBlog().getId() + " not found.")
							.asRuntimeException()
			);
		}

		Document replacement = new Document().append("_id", new ObjectId(request.getBlog().getId()))
											 .append("authorId", request.getBlog().getAuthorId())
											 .append("title", request.getBlog().getTitle())
											 .append("content", request.getBlog().getContent());

		System.out.println("Replacing Blog in Database...");

		collection.replaceOne(eq("_id", result.getObjectId("_id")), replacement);

		System.out.println("Replaced! Sending as response");

		responseObserver.onNext(UpdateBlogResponse.newBuilder()
												  .setBlog(documentToBlog(replacement))
												  .build());
		responseObserver.onCompleted();
	}

	@Override
	public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {
		System.out.println("Received Delete Blog Request");
		System.out.println("Searching for a Blog to delete...");
		DeleteResult result = null;

		try {
			result = collection.deleteOne(eq("_id", new ObjectId(request.getBlogId())));;
		} catch (Exception ex) {
			System.out.println("Blog id: " + request.getBlogId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with the corresponding id was not found.")
							.augmentDescription(ex.getLocalizedMessage())
							.asRuntimeException()
			);
		}


		if (result.getDeletedCount() == 0){
			System.out.println("Blog id: " + request.getBlogId() + " not found.");

			responseObserver.onError(
					Status.NOT_FOUND
							.withDescription("The blog with the corresponding id was not found.")
							.augmentDescription("Blog id: " + request.getBlogId() + " not found.")
							.asRuntimeException()
			);
		}

		System.out.println("Deleted! Sending as response");

		responseObserver.onNext(DeleteBlogResponse.newBuilder().build());
		responseObserver.onCompleted();
	}

	@Override
	public void findAllBlog(FindAllBlogRequest request, StreamObserver<FindAllBlogResponse> responseObserver) {
		System.out.println("Received Find All Blog Request");

		collection.find().forEach(document -> responseObserver.onNext(FindAllBlogResponse.newBuilder().setBlog(documentToBlog(document)).build()));

		responseObserver.onCompleted();
	}

	private Blog documentToBlog(Document document) {
		return Blog.newBuilder()
				   .setId(document.getObjectId("_id").toString())
				   .setAuthorId(document.getString("authorId"))
				   .setContent(document.getString("content"))
				   .setTitle(document.getString("title"))
				   .build();
	}
}