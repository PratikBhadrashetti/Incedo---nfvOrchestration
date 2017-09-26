"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var core_1 = require('@angular/core');
var CheckComponent = (function () {
    function CheckComponent() {
    }
    __decorate([
        core_1.Input(), 
        __metadata('design:type', Boolean)
    ], CheckComponent.prototype, "checked", void 0);
    CheckComponent = __decorate([
        core_1.Component({
            selector: 'br-check',
            template: "\n        <svg:svg viewBox=\"0 0 100 100\" class=\"cp-check\">\n            <path class=\"cp-check-check\" d=\"m 7.42,52.96 28.16,31.24 50.26,-68.07\"\n                [@checked]=\"checked ? 'on' : 'off'\"/>\n            <path class=\"cp-check-box\" d=\"M 0,0 100,0 100,100 0,100z\"/>\n        </svg:svg>\n    ",
            styles: ["\n        :host {\n            display: inline-block;\n            width: 1em;\n            height: 1em;\n        }\n        .cp-check-box {\n            fill: none;\n            stroke: #000000;\n            stroke-width: 10;\n        }\n        .cp-check-check {\n            stroke: #30cc10;\n            stroke-width: 20px;\n            stroke-linecap: square;\n            stroke-linejoin: miter;\n            fill: none;\n\n            stroke-dasharray: 126.67, 126.67;\n            stroke-dashoffset: 0;\n\n            transform-origin: 50% 50%;\n        }\n    "],
            animations: [
                core_1.trigger('checked', [
                    core_1.state('on', core_1.style({ strokeDashoffset: '*' })),
                    core_1.state('off', core_1.style({ strokeDashoffset: '126.37', opacity: 0 })),
                    core_1.transition('off => on', [
                        core_1.style({ opacity: 1, transform: 'scale(1)' }),
                        core_1.animate('250ms ease'),
                    ]),
                    core_1.transition('on => off', [
                        core_1.animate('250ms ease', core_1.style({ transform: 'scale(2)', opacity: 0 })),
                    ]),
                ]),
            ],
            changeDetection: core_1.ChangeDetectionStrategy.OnPush,
        }), 
        __metadata('design:paramtypes', [])
    ], CheckComponent);
    return CheckComponent;
}());
exports.CheckComponent = CheckComponent;
//# sourceMappingURL=check.component.js.map