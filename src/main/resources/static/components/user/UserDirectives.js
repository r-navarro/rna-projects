angular.module('carManagement.user')
.directive('passwordVerify', [function () {
    return {
      require: 'ngModel',
      link: function (scope, elem, attrs, ctrl) {
        var firstPassword = '#' + attrs.passwordVerify;
        elem.add(firstPassword).on('keyup', function () {
          scope.$apply(function () {
            var v = elem.val()===$(firstPassword).val();
            ctrl.$setValidity('passwordMatch', v);
          });
        });
      }
    }
}]);