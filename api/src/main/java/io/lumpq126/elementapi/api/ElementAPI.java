package io.lumpq126.elementapi.api;

import io.lumpq126.elementapi.api.elements.Element;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Element 시스템의 모든 기능을 제공하는 중앙 API 클래스.
 * 싱글턴 패턴으로 구현되어 어디서든 동일한 인스턴스에 접근할 수 있습니다.
 */
public final class ElementAPI {

    private static final ElementAPI instance = new ElementAPI();

    public static ElementAPI getInstance() {
        return instance;
    }

    // 관계 상수
    public static final int GENERAL = 1;
    public static final int WEAKNESS = 2;
    // ... (기존과 동일하게 STRENGTH 등 다른 상수 정의) ...

    private final Map<String, Element> registeredElements = new HashMap<>();
    private final Map<Element, Map<Element, Integer>> relationMatrix = new HashMap<>();

    private Element defaultElement;

    private ElementAPI() {
        // private 생성자로 외부에서 인스턴스화 방지
    }

    /**
     * 새로운 Element를 시스템에 등록합니다.
     * 이미 같은 ID로 등록된 Element가 있다면 등록에 실패합니다.
     *
     * @param element 등록할 Element 인스턴스
     * @return 등록 성공 여부
     */
    public boolean registerElement(Element element) {
        if (registeredElements.containsKey(element.getId())) {
            // 경고 메시지 로깅 (예: getLogger().warning(...))
            return false;
        }
        registeredElements.put(element.getId(), element);
        // 첫 번째로 등록되는 요소를 기본값으로 설정하거나, 별도의 기본 요소를 등록
        if (defaultElement == null) {
            defaultElement = element;
        }
        return true;
    }

    /**
     * ID를 사용해 등록된 Element를 조회합니다.
     * @param id 조회할 Element의 ID
     * @return Optional<Element> (존재하지 않을 수 있으므로 Optional 사용)
     */
    public Optional<Element> getElement(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(registeredElements.get(id.toUpperCase()));
    }

    /**
     * 시스템의 기본 Element를 반환합니다.
     * @return 기본 Element. 등록된 Element가 하나도 없으면 null일 수 있음.
     */
    public Element getDefaultElement() {
        return defaultElement;
    }

    /**
     * 두 Element 간의 관계를 설정합니다.
     * @param from 시작 Element
     * @param to 대상 Element
     * @param relation 관계 값
     */
    public void setRelation(Element from, Element to, int relation) {
        // 두 Element가 모두 등록되었는지 확인하는 로직 추가 가능
        relationMatrix.computeIfAbsent(from, k -> new HashMap<>()).put(to, relation);
    }

    // getRelation, getDamageMultiplier 등 다른 메서드는 이전 ElementManager와 거의 동일하게 구현합니다.
    public int getRelation(Element from, Element to) {
        return relationMatrix.getOrDefault(from, Map.of()).getOrDefault(to, GENERAL);
    }
}