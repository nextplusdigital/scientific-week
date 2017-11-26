(function() {
    'use strict';

    angular
        .module('romaninisistemasApp')
        .controller('CorDeleteController',CorDeleteController);

    CorDeleteController.$inject = ['$uibModalInstance', 'entity', 'Cor'];

    function CorDeleteController($uibModalInstance, entity, Cor) {
        var vm = this;

        vm.cor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Cor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
