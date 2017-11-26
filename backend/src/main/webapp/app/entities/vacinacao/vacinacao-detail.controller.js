(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('VacinacaoDetailController', VacinacaoDetailController);

    VacinacaoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Vacinacao', 'Pessoa'];

    function VacinacaoDetailController($scope, $rootScope, $stateParams, previousState, entity, Vacinacao, Pessoa) {
        var vm = this;

        vm.vacinacao = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('romaninisistemasApp:vacinacaoUpdate', function(event, result) {
            vm.vacinacao = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
