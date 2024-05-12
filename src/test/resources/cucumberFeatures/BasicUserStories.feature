Feature: Basic User Stories

  Scenario: Simple comparison
    Given database has these companies:
      | name      |
      | Apple     |
      | Google    |
      | Microsoft |
    And database has these news:
      | link  | text                        |
      | link1 | Google is good              |
      | link2 | Apple and Microsoft are bad |
#    could become Then or removed, but now used for mocking
    And there are such references:
      | name      | link  | text                        |
      | Google    | link1 | Google is good              |
      | Apple     | link2 | Apple and Microsoft are bad |
      | Microsoft | link2 | Apple and Microsoft are bad |
    When asked about Google and Microsoft
    Then Google should be better then Microsoft
    When asked about Apple
    Then Apple should be same as Microsoft
    Then Apple should be worse then Google