Feature: Basic User Stories

  Scenario: Simple comparison
    Given database has these companies:
      | name      |
      | Apple     |
      | Google    |
      | Microsoft |
    And database has these news:
      | link  | text                        | title                     |
      | link1 | Google is good              | Google title              |
      | link2 | Apple and Microsoft are bad | Apple and Microsoft title |
#    could become Then or removed, but now used for mocking
    And there are such references:
      | name      | link  | text                        | title                     |
      | Google    | link1 | Google is good              | Google title              |
      | Apple     | link2 | Apple and Microsoft are bad | Apple and Microsoft title |
      | Microsoft | link2 | Apple and Microsoft are bad | Apple and Microsoft title |
    When asked about Google and Microsoft
    Then Google should be better then Microsoft
    When asked about Apple
    Then Apple should be same as Microsoft
    Then Apple should be worse then Google