Feature: Signup
  Background:
    Given User is on Xalts page "https://xaltsocnportal.web.app/"
    And User clicks the Get started button to navigate signup page

  @ValidCredentials
  Scenario Outline: Signup with various credentials
    When User enters username as "<email>" and password as "<password>"
    Then User enters the confirm password as "<confirmPassword>"
    Then User clicks the signup button
    Then User clicks the signout button

    Examples:
      | email                   | password          | confirmPassword    |
      | validuser123@example.com  | StrongPass12345!    | StrongPass12345!     |
      | invaliduser.com        | StrongPass123!    | StrongPass123!     |
      | weakpass@example.com   | 123               | 123                |
      | validuser@example.com  | WrongPassword     | WrongPassword      |
