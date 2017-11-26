(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('VacinacaoDeleteController',VacinacaoDeleteController);

    VacinacaoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Vacinacao'];

    function VacinacaoDeleteController($uibModalInstance, entity, Vacinacao) {
        var vm = this;

        vm.vacinacao = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Vacinacao.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
