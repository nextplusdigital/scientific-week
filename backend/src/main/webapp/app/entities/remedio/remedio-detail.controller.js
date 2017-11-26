(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('RemedioDetailController', RemedioDetailController);

    RemedioDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Remedio'];

    function RemedioDetailController($scope, $rootScope, $stateParams, previousState, entity, Remedio) {
        var vm = this;

        vm.remedio = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:remedioUpdate', function(event, result) {
            vm.remedio = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
