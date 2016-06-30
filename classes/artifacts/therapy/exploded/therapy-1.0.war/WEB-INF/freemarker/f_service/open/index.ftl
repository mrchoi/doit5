<@ap.py_header_load_script /><#--  -->

<script src="${Request.svcResPath}js/offset.js"></script>

<script type="text/javascript" src="${Request.svcResPath}js/bootstrap-select.js"></script>
<script src="${Request.svcResPath}js/owl.carousel.min.js"></script>
<script type="text/javascript" src="${Request.svcResPath}js/slider.js"></script>
<script type="text/javascript" src="${Request.svcResPath}js/testimonial.js"></script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_top_bar loginSession=false/><#-- 상위 메뉴 -->
<@ap.py_page_navigation /><#-- navigation 메뉴 -->
<!------------------------------------------------------------------------------------------->

<div class="modal fade" id="notice_modal" tabindex="-1" role="dialog" aria-labelledby="remoteModalLabel" aria-hidden="true" style="padding-top: 15%;">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h3 class="modal-title" id="myModalLabel">플리마인드 오픈</h3>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-12">
                        플리마인드 오픈 했습니다. 많은 관심 부탁 드립니다.
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" data-box-number="">확인</button>
            </div>
        </div>
    </div>
</div>

<div class="slider-bg"><!-- slider start-->
    <div id="slider" class="owl-carousel owl-theme slider">
        <#--<div class="item"><img src="${Request.svcResPath}images/hero-image-3.jpg" alt="Wedding couple just married"></div>
        <div class="item"><img src="${Request.svcResPath}images/hero-image-2.jpg" alt="Wedding couple just married"></div>-->
         <div class="item"><img src="${Request.svcResPath}images/hero-image-3.jpg" alt="plymind"></div>
        <div class="item"><img src="${Request.svcResPath}images/hero-image.jpg" alt="plymind"></div>
        <div class="item"><img src="${Request.svcResPath}images/hero-image-2.jpg" alt="plymind"></div>
    </div>
    <#--<div class="find-section"><!-- Find search section&ndash;&gt;
        <div class="container">
            <div class="row">
                <div class="col-md-offset-1 col-md-10 finder-block">
                    <div class="finder-caption">
                        <h1>Find your perfect Wedding Vendor</h1>
                        <p>Over <strong>1200+ Wedding Vendor </strong>for you special date &amp; Find the perfect venue &amp; save you date.</p>
                    </div>
                    <div class="finderform">
                        <form>
                            <div class="col-md-5 no-padding">
                                <select class="form-control selectpicker">
                                    <option>Select Vendor Category</option>
                                    <option value="Venue">Venue</option>
                                    <option value="Photographer">Photographer</option>
                                    <option value="Cake">Cake</option>
                                    <option value="Dj">Dj</option>
                                    <option value="Florist">Florist</option>
                                    <option value="Videography">Videography</option>
                                    <option value="jewellery">Jewellery</option>
                                    <option value="Gifts">Gifts</option>
                                    <option value="Dresses">Dresses</option>
                                </select>
                            </div>
                            <div class="form-group col-md-5 no-padding">
                                <input type="text" class="form-control" placeholder="Zip Code">
                            </div>
                            <button type="submit" class="btn tp-btn-default tp-btn-lg">Find Vendors</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div><!-- /.Find search section&ndash;&gt;-->
</div>
<!-- slider end-->
<div class="spacer feature-section" style="padding-bottom:0px;"><!-- Feature Blog Start -->
    <div class="container">
        <div class="row">
            <div class="col-md-12 tp-title-left">
                <h2>상담이 필요한 현실</h2>
            </div>
        </div>
        <div class="row feature-left">
            <div class="col-md-5">
                <div class="well-box">
                    <p> 한국은 OECD 국가 중 자살률이 제일 높은 <br>
                        국가입니다.<br>
                        하지만 정신과나 상담센터에서 도움을 구하는 <br>
                        사람들은 많지 않습니다.<br>
                        치료 기록이 남아 사회생활이 힘들어 질까봐....<br>
                        이상한 사람으로 보이게 될까봐....<br>
                        어떻게 도움을 받아야 할지 몰라서...<br>
                        여러 오해들로....
                    </p>
                </div>
            </div>
            <div class="col-md-7" style="font:20px 'BaeDalJua','Istok Web',sans-serif; line-height: 35px;">
                <br>
                <p>플리마인드는 당신의 Mind에<br>
                공감을 Play하고<br>
                위안을 Play하고<br>
                행복을 Play하고<br>
                사소한 배려로 Mind를 선물하고자 합니다</p>
            </div>
        </div>
    </div>
</div><!-- Feature Blog End -->
<div class="tp-section spacer" style="padding-bottom:10px;">
    <div class="container">
        <div class="row">
            <div class="col-md-12 tp-title-left">
                <h2>플리마인드 소개</h2>
            </div>
        </div>
        <div class="row ">
            <div class="col-md-12 vendor-box" style="font:20px 'NanumBarunpenB','Istok Web',sans-serif; line-height: 30px;">
                <p>문제가 있어도 힘들다는 생각이 들어도 상담센터를 찾아가는 것은 엄청난 용기가 필요한 일입니다.<br>
                우리의 마인드는 수 많은 메세지로 힘듦을 표현합니다. 그러나 다만, 그것을 놓치지 않고 귀 기울이기는 쉽지가 않습니다.<br>
                플리마인드는 첫 상담을 임상심리전문가 선생님이 진행하고 마인드가 전하는 메세지를 듣겠습니다.<br>
                플리마인드는 첫 걸음을 떼기 어려운 여러분들의 동반자가 되어 치유될수 있는 더 좋은 방법을 찾아드릴 것입니다.
                </p>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function() {
        //$('#notice_modal').modal('show');
    });

</script>

<!------------------------------------------------------------------------------------------->
<@ap.py_page_footer />
<!------------------------------------------------------------------------------------------->
