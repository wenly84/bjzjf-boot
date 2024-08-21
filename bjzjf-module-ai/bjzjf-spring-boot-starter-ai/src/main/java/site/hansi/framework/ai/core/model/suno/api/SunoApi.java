package site.hansi.framework.ai.core.model.suno.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrPool;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpRequest;
//import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Suno API
 * <p>
 * 对接 Suno Proxy：<a href="https://github.com/gcui-art/suno-api">suno-api</a>
 *
 * @author xiaoxin
 */
@Slf4j
public class SunoApi {

    private final WebClient webClient;

//    private final Predicate<HttpStatusCode> STATUS_PREDICATE = status -> !status.is2xxSuccessful();
//
//    private final Function<Object, Function<ClientResponse, Mono<? extends Throwable>>> EXCEPTION_FUNCTION =
//            reqParam -> response -> response.bodyToMono(String.class).handle((responseBody, sink) -> {
//                HttpRequest request = response.request();
//                log.error("[suno-api] 调用失败！请求方式:[{}]，请求地址:[{}]，请求参数:[{}]，响应数据: [{}]",
//                        request.getMethod(), request.getURI(), reqParam, responseBody);
//                sink.error(new IllegalStateException("[suno-api] 调用失败！"));
//            });

    public SunoApi(String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders((headers) -> headers.setContentType(MediaType.APPLICATION_JSON))
                .build();
    }

    public List<MusicData> generate(MusicGenerateRequest request) {
        return this.webClient.post()
                .uri("/api/generate")
                .body(Mono.just(request), MusicGenerateRequest.class)
                .retrieve()
                //.onStatus(STATUS_PREDICATE, EXCEPTION_FUNCTION.apply(request))
                .bodyToMono(new ParameterizedTypeReference<List<MusicData>>() {
                })
                .block();
    }

    public List<MusicData> customGenerate(MusicGenerateRequest request) {
        return this.webClient.post()
                .uri("/api/custom_generate")
                .body(Mono.just(request), MusicGenerateRequest.class)
                .retrieve()
                //.onStatus(STATUS_PREDICATE, EXCEPTION_FUNCTION.apply(request))
                .bodyToMono(new ParameterizedTypeReference<List<MusicData>>() {
                })
                .block();
    }

    public LyricsData generateLyrics(String prompt) {
        return this.webClient.post()
                .uri("/api/generate_lyrics")
                .body(Mono.just(new MusicGenerateRequest(prompt)), MusicGenerateRequest.class)
                .retrieve()
                //.onStatus(STATUS_PREDICATE, EXCEPTION_FUNCTION.apply(prompt))
                .bodyToMono(LyricsData.class)
                .block();
    }

    public List<MusicData> getMusicList(List<String> ids) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/get")
                        .queryParam("ids", CollUtil.join(ids, StrPool.COMMA))
                        .build())
                .retrieve()
                //.onStatus(STATUS_PREDICATE, EXCEPTION_FUNCTION.apply(ids))
                .bodyToMono(new ParameterizedTypeReference<List<MusicData>>() {
                })
                .block();
    }

    public LimitUsageData getLimitUsage() {
        return this.webClient.get()
                .uri("/api/get_limit")
                .retrieve()
                //.onStatus(STATUS_PREDICATE, EXCEPTION_FUNCTION.apply(null))
                .bodyToMono(LimitUsageData.class)
                .block();
    }

    /**
     * 根据提示生成音频
     *
     * @param prompt           用于生成音乐音频的提示
     * @param tags             音乐风格
     * @param title            音乐名称
     * @param model            模型
     * @param waitAudio        false 表示后台模式，仅返回音频任务信息，需要调用 get API 获取详细的音频信息。
     *                         true 表示同步模式，API 最多等待 100s，音频生成完毕后直接返回音频链接等信息，建议在 GPT 等 agent 中使用。
     * @param makeInstrumental 指示音乐音频是否为定制，如果为 true，则从歌词生成，否则从提示生成
     */
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    public static class MusicGenerateRequest{
    	
        public String getPrompt() {
			return prompt;
		}

		public void setPrompt(String prompt) {
			this.prompt = prompt;
		}

		public String getTags() {
			return tags;
		}

		public void setTags(String tags) {
			this.tags = tags;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String model) {
			this.model = model;
		}

		public boolean isWaitAudio() {
			return waitAudio;
		}

		public void setWaitAudio(boolean waitAudio) {
			this.waitAudio = waitAudio;
		}

		public boolean isMakeInstrumental() {
			return makeInstrumental;
		}

		public void setMakeInstrumental(boolean makeInstrumental) {
			this.makeInstrumental = makeInstrumental;
		}

		private String prompt;
        private String tags;
        private String title;
        private String model;
        @JsonProperty("wait_audio") 
        private boolean waitAudio;
        @JsonProperty("make_instrumental") 
        private boolean makeInstrumental;
    	
    	public MusicGenerateRequest(
                String prompt,
                String tags,
                String title,
                String model,
                @JsonProperty("wait_audio") boolean waitAudio,
                @JsonProperty("make_instrumental") boolean makeInstrumental
        ) {
    		this.prompt = prompt;
    		this.tags = tags;
    		this.title = title;
    		this.model = model;
    		this.waitAudio = waitAudio;
    		this.makeInstrumental = makeInstrumental;
    	}

        public MusicGenerateRequest(String prompt) {
        	this.prompt = prompt;
    		this.waitAudio = false;
    		this.makeInstrumental = false;
        }

        public MusicGenerateRequest(String prompt, String model, boolean makeInstrumental) {
    		this.prompt = prompt;
    		this.model = model;
    		this.makeInstrumental = false;
        }
        
        public MusicGenerateRequest(String prompt, String tags, String title, String model) {
    		this.prompt = prompt;
    		this.tags = tags;
    		this.title = title;
    		this.model = model;
    		this.waitAudio = false;
    		this.makeInstrumental = false;
        }

    }

    /**
     * Suno API 响应的音频数据
     *
     * @param id                   音乐数据的 ID
     * @param title                音乐音频的标题
     * @param imageUrl             音乐音频的图片 URL
     * @param lyric                音乐音频的歌词
     * @param audioUrl             音乐音频的 URL
     * @param videoUrl             音乐视频的 URL
     * @param createdAt            音乐音频的创建时间
     * @param modelName            模型名称
     * @param status               submitted、queued、streaming、complete
     * @param gptDescriptionPrompt 描述词
     * @param prompt               生成音乐音频的提示
     * @param type                 操作类型
     * @param tags                 音乐类型标签
     * @param duration             音乐时长
     */
    public class MusicData {
    	
        public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImageUrl() {
			return imageUrl;
		}

		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}

		public String getLyric() {
			return lyric;
		}

		public void setLyric(String lyric) {
			this.lyric = lyric;
		}

		public String getAudioUrl() {
			return audioUrl;
		}

		public void setAudioUrl(String audioUrl) {
			this.audioUrl = audioUrl;
		}

		public String getVideoUrl() {
			return videoUrl;
		}

		public void setVideoUrl(String videoUrl) {
			this.videoUrl = videoUrl;
		}

		public String getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(String createdAt) {
			this.createdAt = createdAt;
		}

		public String getModelName() {
			return modelName;
		}

		public void setModelName(String modelName) {
			this.modelName = modelName;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getGptDescriptionPrompt() {
			return gptDescriptionPrompt;
		}

		public void setGptDescriptionPrompt(String gptDescriptionPrompt) {
			this.gptDescriptionPrompt = gptDescriptionPrompt;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getPrompt() {
			return prompt;
		}

		public void setPrompt(String prompt) {
			this.prompt = prompt;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTags() {
			return tags;
		}

		public void setTags(String tags) {
			this.tags = tags;
		}

		public Double getDuration() {
			return duration;
		}

		public void setDuration(Double duration) {
			this.duration = duration;
		}

		private String id;
        private String title;
        @JsonProperty("image_url") 
        private String imageUrl;
        private String lyric;
        @JsonProperty("audio_url") 
        private String audioUrl;
        @JsonProperty("video_url") 
        private String videoUrl;
        @JsonProperty("created_at") 
        private String createdAt;
        @JsonProperty("model_name") 
        private String modelName;
        private String status;
        @JsonProperty("gpt_description_prompt") 
        private String gptDescriptionPrompt;
        @JsonProperty("error_message") 
        private String errorMessage;
        private String prompt;
        private String type;
        private String tags;
        private Double duration;
    	
    	public MusicData(
                String id,
                String title,
                @JsonProperty("image_url") String imageUrl,
                String lyric,
                @JsonProperty("audio_url") String audioUrl,
                @JsonProperty("video_url") String videoUrl,
                @JsonProperty("created_at") String createdAt,
                @JsonProperty("model_name") String modelName,
                String status,
                @JsonProperty("gpt_description_prompt") String gptDescriptionPrompt,
                @JsonProperty("error_message") String errorMessage,
                String prompt,
                String type,
                String tags,
                Double duration
        ) {
    		this.id = id;
    		this.title = title;
    		this.imageUrl = imageUrl;
    		this.lyric = lyric;
    		this.audioUrl = audioUrl;
    		this.videoUrl = videoUrl;
    		this.createdAt = createdAt;
    		this.modelName = modelName;
    		this.status = status;
    		this.gptDescriptionPrompt = gptDescriptionPrompt;
    		this.errorMessage = errorMessage;
    		this.prompt = prompt;
    		this.type = type;
    		this.tags = tags;
    		this.duration = duration;
    		
    	}
    	
    }

    /**
     * Suno API 响应的歌词数据。
     *
     * @param text   歌词
     * @param title  标题
     * @param status 状态
     */
    public class LyricsData{
    	
        public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		private String text;
        private String title;
        private String status;
        
    	public LyricsData(
                String text,
                String title,
                String status
        ) {
    		this.text = text;
    		this.title = title;
    		this.status = status;
    	}
    }

    /**
     * Suno API 响应的限额数据，目前每日免费 50
     */
    public class LimitUsageData {
    	
        public Long getCreditsLeft() {
			return creditsLeft;
		}

		public void setCreditsLeft(Long creditsLeft) {
			this.creditsLeft = creditsLeft;
		}

		public String getPeriod() {
			return period;
		}

		public void setPeriod(String period) {
			this.period = period;
		}

		public Long getMonthlyLimit() {
			return monthlyLimit;
		}

		public void setMonthlyLimit(Long monthlyLimit) {
			this.monthlyLimit = monthlyLimit;
		}

		public Long getMonthlyUsage() {
			return monthlyUsage;
		}

		public void setMonthlyUsage(Long monthlyUsage) {
			this.monthlyUsage = monthlyUsage;
		}

		@JsonProperty("credits_left") 
        private Long creditsLeft;
        private String period;
        @JsonProperty("monthly_limit") 
        private Long monthlyLimit;
        @JsonProperty("monthly_usage") 
        private Long monthlyUsage;
    	
    	public LimitUsageData(
        		
                @JsonProperty("credits_left") Long creditsLeft,
                String period,
                @JsonProperty("monthly_limit") Long monthlyLimit,
                @JsonProperty("monthly_usage") Long monthlyUsage
        ) {
    		this.creditsLeft = creditsLeft;
    		this.period = period;
    		this.monthlyLimit = monthlyLimit;
    		this.monthlyUsage = monthlyUsage;
    		
    	}
    }

}
