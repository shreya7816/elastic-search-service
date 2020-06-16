package com.es.demo.manager;

import com.es.demo.constants.ElasticSearchConstants;
import com.es.demo.model.Member;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class MemberManager {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;

    public String create(Member member) throws Exception {
        GetIndexRequest request = new GetIndexRequest(ElasticSearchConstants.INDEX_NAME);
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        if(!exists){
            createMapping();
        }
        return createDocument(member);
    }

    public boolean createMapping() throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(ElasticSearchConstants.INDEX_NAME);
        request.mapping(ElasticSearchConstants.MAPPING, XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        return createIndexResponse.isAcknowledged();
    }

    public String createDocument(Member member) throws Exception {
        String str = objectMapper.writeValueAsString(member);
        IndexRequest indexRequest = new IndexRequest(ElasticSearchConstants.INDEX_NAME);
        indexRequest.id(member.getId().toString());
        indexRequest.source(str, XContentType.JSON);
        indexRequest.opType(ElasticSearchConstants.OP_TYPE);
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        return indexResponse.getResult().name();
    }

    public Member findById(UUID id) throws Exception {
        GetRequest getRequest = new GetRequest(ElasticSearchConstants.INDEX_NAME, id.toString());
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();
        return objectMapper.convertValue(resultMap, Member.class);
    }

    public List<Member> findAll() throws Exception {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchRequest.indices(ElasticSearchConstants.INDEX_NAME);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(searchResponse);
    }

    public List<Member> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<Member> memberDocuments = new ArrayList<>();
        if (searchHit.length > 0) {
            Arrays.stream(searchHit)
                    .forEach(hit -> {
                                Member member = objectMapper.convertValue(hit.getSourceAsMap(), Member.class);
                                memberDocuments.add(member);
                            }
                    );
        }
        return memberDocuments;
    }

    public String deleteDocument(UUID id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(ElasticSearchConstants.INDEX_NAME, id.toString());
        DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);
        return response.getResult().name();
    }

    public List<Member> findByAmenityId(UUID amenityId) throws IOException {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.nestedQuery(ElasticSearchConstants.AMENITIES, QueryBuilders.boolQuery()
                        .should(QueryBuilders.termQuery(ElasticSearchConstants.AMENITY_ID, amenityId.toString())), ScoreMode.None));
        searchSourceBuilder.query(builder);
        searchRequest.indices(ElasticSearchConstants.INDEX_NAME);
        searchRequest.source(searchSourceBuilder);
        SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
        return getSearchResult(response);
    }

    public String updateDocument(Member document) throws Exception {
        Member resultDocument = findById(document.getId());
        UpdateRequest updateRequest = new UpdateRequest(ElasticSearchConstants.INDEX_NAME, document.getId().toString());
        Map<String, Object> documentMapper = objectMapper.convertValue(document, Map.class);
        updateRequest.doc(documentMapper);
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        return updateResponse.getResult().name();
    }

}
